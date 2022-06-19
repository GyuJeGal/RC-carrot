package com.example.demo.src.user;

import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkPhoneNumber(String phoneNumber) {
        String checkPhoneNumberQuery = "select exists (select userId from User where phoneNumber = ?)";
        String checkPhoneNumberParam = phoneNumber;

        return this.jdbcTemplate.queryForObject(checkPhoneNumberQuery,
                int.class,
                checkPhoneNumberParam);
    }

    public int createUser(PostUserReq postUserReq) {

        //사용자 테이블 추가
        String createUserQuery = "insert into User(nickName, phoneNumber, profileImgUrl) values (?,?,?)";
        Object[] createUserParams;

        if(postUserReq.getProfileImg() == null) {
            createUserParams = new Object[] {postUserReq.getNickName(), postUserReq.getPhoneNumber(), null};
        }
        else {
            createUserParams = new Object[] {postUserReq.getNickName(), postUserReq.getPhoneNumber(), postUserReq.getProfileImg()};
        }

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        //사용자 테이블에 데이터 insert 후 테이블의 마지막 auto_increment 값 불러오기
        String lastInsertIdQuery = "select last_insert_id()";
        int userId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        //회원 가입에서 사용자도 추가하지만 사용자에 대한 주소 또한 추가한다
        String createAddressQuery = "insert into Address(userId, addressName, latitude, longitude) values (?,?,?,?)";
        Object[] createAddressParams = new Object[] {userId, postUserReq.getAddressName(), 36, 37};

        this.jdbcTemplate.update(createAddressQuery, createAddressParams);

        return userId;

    }



    public GetUserRes getUser(int userId) {
        String getUserQuery = "select U.profileImgUrl, U.nickName, U.mannerTemp, U.reTradeRate, U.responseRate,\n" +
                "                case\n" +
                "                when TIMESTAMPDIFF(WEEK , U.lastActive, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(WEEK , U.lastActive, CURRENT_TIMESTAMP) + 1, '주 이내 활동')\n" +
                "                when TIMESTAMPDIFF(DAY , U.lastActive, CURRENT_TIMESTAMP) >= 3 then '1주 이내 활동'\n" +
                "                else '3일 이내 활동' end as lastActive, DATE_FORMAT(U.createAt, '%Y년 %c월 %e일 가입') as userCreateAt, GB.countBadge, SP.countSellPost\n" +
                "                from User  U\n" +
                "                inner join (select userId, count(getBadgeId) as countBadge from User left outer join GetBadge using(userId) group by (userId)) as GB using(userId)\n" +
                "                inner join (select userId, count(sellPostId) as countSellPost from User left outer join SellPost using (userId) group by (userId)) as SP using(userId)\n" +
                "                where userId = ?";
        int getUserParam = userId;

        GetUserRes getUserRes = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        userId,
                        rs.getString("nickName"),
                        rs.getDouble("mannerTemp"),
                        rs.getDouble("reTradeRate"),
                        rs.getDouble("responseRate"),
                        rs.getString("lastActive"),
                        rs.getString("userCreateAt"),
                        rs.getInt("countBadge"),
                        rs.getInt("countSellPost")),
                getUserParam);

        //사용자가 등록한 주소가 두개일때
        if(isTwoAddress(userId) == 2) {
            String getAddressQuery = "select addressName, certifyTime from Address where userId = ?";
            int getAddressParam = userId;

            List<Address> addressList = this.jdbcTemplate.query(getAddressQuery,
                    (rs, rowNum) -> new Address(rs.getString("addressName"), rs.getInt("certifyTime")),
                    getAddressParam);

            getUserRes.setAddressName1(addressList.get(0).getAddressName());
            getUserRes.setAddressName2(addressList.get(1).getAddressName());
            getUserRes.setCertifyTime1(addressList.get(0).getCertifyTime());
            getUserRes.setCertifyTime2(addressList.get(1).getCertifyTime());

        }
        //사용자가 등록한 주소가 한개일때
        else {
            getUserRes.setAddressName2(null);
            getUserRes.setCertifyTime2(null);

            String getAddressQuery = "select addressName, certifyTime from Address where userId = ?";
            int getAddressParam = userId;

            Address address = this.jdbcTemplate.queryForObject(getAddressQuery,
                    (rs, rowNum) -> new Address(rs.getString("addressName"), rs.getInt("certifyTime")),
                    getAddressParam);

            getUserRes.setAddressName1(address.getAddressName());
            getUserRes.setCertifyTime1(address.getCertifyTime());

        }

        return getUserRes;
    }

    public int isExistUser(int userId) {
        String getUserQuery = "select exists (select userId from User where userId = ?)";
        int getUserParam = userId;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                int.class,
                getUserParam);
    }

    public int isTwoAddress(int userId) {
        String checkAddressQuery = "select count(addressName) from Address where userId = ? and status = 1";
        int checkAddressParam = userId;

        return this.jdbcTemplate.queryForObject(checkAddressQuery,
                int.class,
                checkAddressParam);
    }

    public void modifyNickName(PatchUserReq patchUserReq) {
        String updateQuery = "update User set nickName = ? where userId = ?";
        Object[] updateParams = {patchUserReq.getNickName(), patchUserReq.getUserId()};

        this.jdbcTemplate.update(updateQuery, updateParams);
    }

    public void modifyProfile(PatchUserReq patchUserReq) {
        String updateQuery = "update User set profileImgUrl = ? where userId = ?";
        Object[] updateParams = new Object[]{patchUserReq.getProfileImg(), patchUserReq.getUserId()};

        this.jdbcTemplate.update(updateQuery, updateParams);
    }

    public void modifyUser(PatchUserReq patchUserReq) {
        String updateQuery = "update User set nickName = ?, profileImgUrl = ? where userId = ?";
        Object[] updateParams;

        if(patchUserReq.getProfileImg() == null) {
            updateParams = new Object[]{patchUserReq.getNickName(), null, patchUserReq.getUserId()};
        }
        else {
            updateParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getProfileImg(), patchUserReq.getUserId()};
        }

        this.jdbcTemplate.update(updateQuery, updateParams);
    }

    public void deleteUser(int userId) {
        String deleteUserQuery = "update User set status = -1 where userId = ?";
        int deleteUserParam = userId;

        this.jdbcTemplate.update(deleteUserQuery, deleteUserParam);
    }

    public void modifyUserEmail(PatchUserEmail patchUserEmail) {
        String modifyEmailQuery = "update User set emailAdd = ? where userId = ?";
        Object[] modifyEmailParams = new Object[]{patchUserEmail.getEmail(), patchUserEmail.getUserId()};

        this.jdbcTemplate.update(modifyEmailQuery, modifyEmailParams);
    }

    public void modifyPhoneNumber(PatchPhoneNumber patchPhoneNumber) {
        String modifyPhoneNumberQuery = "update User set phoneNumber = ? where userId = ?";
        Object[] modifyPhoneNumberParams = new Object[]{patchPhoneNumber.getPhoneNumber(), patchPhoneNumber.getUserId()};

        this.jdbcTemplate.update(modifyPhoneNumberQuery, modifyPhoneNumberParams);
    }


    public List<GetUsersBuyList> getBuyList(int userId) {
        String getBuyListQuery = "select sellPostId, imgUrl, title, addressName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(DAY, SP.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, SP.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "           when TIMESTAMPDIFF(HOUR, SP.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(HOUR, SP.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, SP.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(MINUTE, SP.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "           else concat(TIMESTAMPDIFF(HOUR, SP.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "           end as createAt, price, if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes\n" +
                "         from Buy B\n" +
                "         inner join SellPost SP using(sellPostId)\n" +
                "         inner join (select sellPostId, min(sellPostImgId), imgUrl from SellPostImg group by sellPostId) SPI using(sellPostId)\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where B.userId = ?";
        int getBuyListParam = userId;

        List<GetUsersBuyList> getUsersBuyLists = this.jdbcTemplate.query(getBuyListQuery,
                (rs, rowNum) -> new GetUsersBuyList(
                        rs.getInt("sellPostId"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("addressName"),
                        rs.getString("createAt"),
                        rs.getInt("price"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes")),
                getBuyListParam);

        return getUsersBuyLists;

    }

    public List<GetUserSellList> getSellList(int userId) {
        String getSellListQuery = "select SP.sellPostId, SP.status, SPI.imgUrl, SP.title, SP.addressName, SP.pullUpTime,\n" +
                "       case\n" +
                "           when pullUpTime > 0 then case\n" +
                "                                        when TIMESTAMPDIFF(DAY, pullUpAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, pullUpAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "                                        when TIMESTAMPDIFF(MINUTE , pullUpAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , pullUpAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "                                        when TIMESTAMPDIFF(SECOND, pullUpAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , pullUpAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "                                        else concat(TIMESTAMPDIFF(SECOND, pullUpAt, CURRENT_TIMESTAMP), '초 전') end\n" +
                "           else case\n" +
                "                    when TIMESTAMPDIFF(DAY, SP.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, SP.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "                    when TIMESTAMPDIFF(MINUTE , SP.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , SP.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "                    when TIMESTAMPDIFF(SECOND, SP.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , SP.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "                    else concat(TIMESTAMPDIFF(SECOND, SP.createAt, CURRENT_TIMESTAMP), '초 전') end\n" +
                "           end as uploadAt, isShare, price, if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes\n" +
                "from SellPost SP\n" +
                "         left outer join (select sellPostId, min(sellPostImgId), imgUrl from SellPostImg group by (sellPostId)) SPI using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where SP.userId = ?";
        int getSellListParam = userId;
        List<GetUserSellList> getUserSellLists = this.jdbcTemplate.query(getSellListQuery,
                ((rs, rowNum) -> new GetUserSellList(
                        rs.getInt("sellPostId"),
                        rs.getInt("status"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("addressName"),
                        rs.getInt("pullUpTime"),
                        rs.getString("uploadAt"),
                        rs.getBoolean("isShare"),
                        rs.getInt("price"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes"))), getSellListParam);
        return getUserSellLists;

    }

    public List<GetUserNotices> getUserNotices(int userId) {
        String getNoticesQuery = "select noticeId, noticeTitle, date_format(createAt,'%Y.%m.%d') as createAt from Notice order by (noticeId) desc";
        return this.jdbcTemplate.query(getNoticesQuery,
                (rs, rowNum) -> new GetUserNotices(
                        rs.getInt("noticeId"),
                        rs.getString("noticeTitle"),
                        rs.getString("createAt")));

    }

    public GetUserNotice getUserNotice(int userId, int noticeId) {
        String getNoticeQuery = "select noticeId, noticeTitle, date_format(createAt,'%Y.%m.%d') as createAt, noticeContents from Notice where noticeId = ?";
        int getNoticeParam = noticeId;
        GetUserNotice getUserNotice = this.jdbcTemplate.queryForObject(getNoticeQuery,
                ((rs, rowNum) -> new GetUserNotice(
                        rs.getInt("noticeId"),
                        rs.getString("noticeTitle"),
                        rs.getString("createAt"),
                        rs.getString("noticeContents"))), getNoticeParam);

        String getImgUrlsQuery = "select imgUrl from NoticeImg where noticeId = ?";
        List<String> imgUrls = this.jdbcTemplate.query(getImgUrlsQuery, (rs, rowNum) -> new String(rs.getString("imgUrl")), getNoticeParam);
        getUserNotice.setImgUrls(imgUrls);

        return getUserNotice;
    }

    public List<GetUserGathers> getUserGather(int userId) {
        String getUserGathersQuery = "select sellPostId, gatheredUserId, profileImgUrl, nickName,\n" +
                "       addressName, SP.status , title, price,\n" +
                "       if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes\n" +
                "from Gather G\n" +
                "         inner join User U on G.gatheredUserId = U.userId\n" +
                "         inner join SellPost SP on U.userId = SP.userId\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where gatheringUserId = ? and G.status = 1";
        int getUserGathersParam = userId;

        List<GetUserGathers> getUserGathers = this.jdbcTemplate.query(getUserGathersQuery,
                ((rs, rowNum) -> new GetUserGathers(
                        rs.getInt("sellPostId"),
                        rs.getInt("gatheredUserId"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickName"),
                        rs.getString("addressName"),
                        rs.getInt("status"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes"))), getUserGathersParam);
        return getUserGathers;
    }

    public GetUserAccountList getUserAccountList(int userId, String searchYearMonth) {
        String getUserAccountListQuery = "select sellPostId, title, price\n" +
                "        from SellPost\n" +
                "        where userId = 6 and status = 3 and DATE_FORMAT(updateAt, '%Y-%m') = ?";
        String getAccountListParam = searchYearMonth;

        List<AccountList> accountLists = this.jdbcTemplate.query(getUserAccountListQuery,
                ((rs, rowNum) -> new AccountList(
                        rs.getInt("sellPostId"),
                        rs.getString("title"),
                        rs.getInt("price"))), getAccountListParam);

        String getSumPriceQuery = "select SUM(price) sumPrice\n" +
                "from SellPost\n" +
                "where userId = 6 and status = 3 and DATE_FORMAT(updateAt, '%Y-%m') = ?";
        int sumPrice = this.jdbcTemplate.queryForObject(getSumPriceQuery, int.class, getAccountListParam);

        GetUserAccountList getUserAccountList = new GetUserAccountList(accountLists, sumPrice);

        return getUserAccountList;
    }

    public List<GetInterestList> getInterestList(int userId) {
        String getInterestQuery = "select SP.sellPostId, SPI.imgUrl, SP.title, SP.addressName, SP.isShare, SP.price, if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes\n" +
                "from Interest I\n" +
                "         inner join SellPost SP using(sellPostId)\n" +
                "         left outer join (select sellPostId, min(sellPostImgId), imgUrl from SellPostImg group by (sellPostId)) SPI using(sellPostId)\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where I.userId = ?";
        int getInterestParam = userId;

        List<GetInterestList> getInterestLists = this.jdbcTemplate.query(getInterestQuery,
                (rs, rowNum) -> new GetInterestList(
                        rs.getInt("sellPostId"),
                        rs.getString("imgUrl"),
                        rs.getString("title"),
                        rs.getString("addressName"),
                        rs.getBoolean("isShare"),
                        rs.getInt("price"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes")), getInterestParam);
        return getInterestLists;
    }
}


