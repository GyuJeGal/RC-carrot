package com.example.demo.src.sellPost;

import com.example.demo.src.sellPost.model.DeleteSellPost;
import com.example.demo.src.sellPost.model.GetSellPostRes;
import com.example.demo.src.sellPost.model.GetSellPostsRes;
import com.example.demo.src.sellPost.model.PostSellPostReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SellPostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int isExistsUser(int userId) {
        String getUserQuery = "select exists (select userId from User where userId = ?)";
        int getUserParam = userId;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                int.class,
                getUserParam);
    }

    public int isExistsSellPost(int sellPostId) {
        String getSellPostQuery = "select exists (select sellPostId from SellPost where sellPostId = ?)";
        int getSellPostParam = sellPostId;

        return this.jdbcTemplate.queryForObject(getSellPostQuery,
                int.class,
                getSellPostParam);
    }


    public List<GetSellPostsRes> getSellPosts(int userId) {
        String getSellPostsQuery = "select SP.sellPostId, title, addressName, price, SPI.imgUrl, if(pullUpTime > 0, true, false) as isPullUp,\n" +
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
                "           end as uploadAt, if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes\n" +
                "from SellPost SP\n" +
                "         inner join (select sellPostId, min(sellPostImgId), imgUrl from SellPostImg group by sellPostId) SPI using(sellPostId)\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where addressName in (select addressName from Address where userId = ? and status = 1 and representAdd = 1) and SP.status=1";

        int getSellPostsParam = userId;

        return this.jdbcTemplate.query(getSellPostsQuery,
                (rs, rowNum) -> new GetSellPostsRes(
                        rs.getInt("sellPostId"),
                        rs.getString("title"),
                        rs.getString("addressName"),
                        rs.getInt("price"),
                        rs.getString("imgUrl"),
                        rs.getBoolean("isPullUp"),
                        rs.getString("uploadAt"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes")),
                getSellPostsParam);
    }


    public GetSellPostRes getSellPost(int userId, int sellPostId) {
        String getSellPostQuery = "select sellPostId, userId, nickName, addressName, mannerTemp, title, categoryName, if(pullUpTime > 0, true, false) as pullUp,\n" +
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
                "           end as uploadAt, if(ISNULL(CC), 0, CC) as countChatting, if(ISNULL(CI), 0, CI) as countLikes, viewTime as countView, price, if(priceProposal=0, false, true) as priceProposal\n" +
                "from SellPost SP\n" +
                "         inner join ProductCategory using (categoryId)\n" +
                "         inner join User using (userId)\n" +
                "         left outer join (select sellPostId, count(roomId) as CC from Room group by (sellPostId)) as R using (sellPostId)\n" +
                "         left outer join (select sellPostId, count(interestId) as CI from Interest group by (sellPostId)) as I using(sellPostId)\n" +
                "where sellPostId = ?";
        int getSellPostParam = sellPostId;

        GetSellPostRes getSellPostRes = this.jdbcTemplate.queryForObject(getSellPostQuery,
                (rs, rowNum) -> new GetSellPostRes(
                        rs.getInt("sellPostId"),
                        rs.getInt("userId"),
                        rs.getString("nickName"),
                        rs.getString("addressName"),
                        rs.getDouble("mannerTemp"),
                        rs.getString("title"),
                        rs.getString("categoryName"),
                        rs.getBoolean("pullUp"),
                        rs.getString("uploadAt"),
                        rs.getInt("countChatting"),
                        rs.getInt("countLikes"),
                        rs.getInt("countView"),
                        rs.getInt("price"),
                        rs.getBoolean("priceProposal")),
                getSellPostParam);

        String getImgUrlQuery = "select imgUrl from SellPostImg where sellPostId = ?";
        List<String> imgUrlList = this.jdbcTemplate.query(getImgUrlQuery,
                ((rs, rowNum) -> new String(rs.getString("imgUrl"))),
                getSellPostParam);
        getSellPostRes.setImgUrl(imgUrlList);

        return getSellPostRes;

    }

    public int checkCategoryId(Integer categoryId) {
        String checkCategoryIdQuery = "select exists(select categoryId from ProductCategory where categoryId = ?)";
        Integer checkParam = categoryId;
        return this.jdbcTemplate.queryForObject(checkCategoryIdQuery, int.class, checkParam);
    }

    public void createSellPost(int userId, PostSellPostReq postSellPostReq) {
        String createSellPostQuery = "insert into SellPost(userId, categoryId, price, priceProposal, title," +
                "contents, addressName) values(?,?,?,?,?,?,?)";
        Object[] createQueryParams = new Object[] {userId, postSellPostReq.getCategoryId(), postSellPostReq.getPrice(),
        postSellPostReq.getPriceProposal(), postSellPostReq.getTitle(), postSellPostReq.getContents(), postSellPostReq.getAddressName()};

        this.jdbcTemplate.update(createSellPostQuery, createQueryParams);

        String getLastSellPostId = "select MAX(sellPostId) from SellPost";
        int sellPostId = this.jdbcTemplate.queryForObject(getLastSellPostId, int.class);

        String insertSellPostImg = "insert into SellPostImg(sellPostId, imgUrl) values(?,?)";

        for (String imgUrl : postSellPostReq.getImgUrlList()) {
            Object[] insertSellPostImgParams = new Object[] {sellPostId, imgUrl};
            this.jdbcTemplate.update(insertSellPostImg, insertSellPostImgParams);
        }

    }

    public int checkSellPost(DeleteSellPost deleteSellPost) {
        String checkSellPostQuery = "select exists(select sellPostId from SellPost where userId = ? and sellPostId = ?)";
        Object[] checkParams = new Object[] {deleteSellPost.getUserId(), deleteSellPost.getSellPostId()};

        return this.jdbcTemplate.queryForObject(checkSellPostQuery, int.class, checkParams);
    }

    public void deleteSellPost(DeleteSellPost deleteSellPost) {
        String deleteSellPostQuery = "update SellPost set status = -1 where sellPostId = ?";
        int queryParam = deleteSellPost.getSellPostId();

        this.jdbcTemplate.update(deleteSellPostQuery, queryParam);
    }
}
