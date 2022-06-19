package com.example.demo.src.posts;

import com.example.demo.src.posts.model.CommentStruct;
import com.example.demo.src.posts.model.GetPostRes;
import com.example.demo.src.posts.model.GetPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> getPosts(int userId) {
        String getPostsQuery = "select P.postId, PT.topicName, P.contents, U.nickName, P.addressName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , P.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "           when TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "           else concat(TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "           end as uploadAt, if(ISNULL(PL.CountLike), 0, PL.CountLike) as countEmpathy,if(ISNULL(C.CountComment), 0, C.CountComment) as countComment\n" +
                "from Post P\n" +
                "         inner join PostTopic PT using(topicId)\n" +
                "         inner join User U using(userId)\n" +
                "         left outer join (select postId, count(postLikeId) CountLike from PostLike group by (postId)) as PL using(postId)\n" +
                "         left outer join (select postId, count(commentId) CountComment from Comment group by (postId)) C using(postId)\n" +
                "where P.addressName in (select addressName from Address where userId = ? and representAdd = 1)";
        int getPostsParam = userId;

        List<GetPostsRes> getPostsResList = this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postId"),
                        rs.getString("topicName"),
                        rs.getString("contents"),
                        rs.getString("nickName"),
                        rs.getString("addressName"),
                        rs.getString("uploadAt"),
                        rs.getInt("countEmpathy"),
                        rs.getInt("countComment")), getPostsParam);

        String getPostIdQuery = "select postId from Post where addressName in (select addressName from Address where userId = ? and representAdd = 1)";

        List<Integer> postIdList = this.jdbcTemplate.query(getPostIdQuery,
                (rs, rowNum) -> new Integer(rs.getInt("postId")), getPostsParam);

        String getImgListQuery = "select ImgUrl from PostImg where postId = ? order by (postImgId) limit 3";

        for (int i = 0; i < postIdList.size(); i++) {
            List<String> imgUrlList = this.jdbcTemplate.query(getImgListQuery,
                    (rs, rowNum) -> new String(rs.getString("ImgUrl")) ,postIdList.get(i));

            getPostsResList.get(i).setImgUrlList(imgUrlList);
        }

        return getPostsResList;
    }

    public List<GetPostsRes> getPostsTopic(int userId, String topicName) {

        String getPostsTopicQuery = "select P.postId, PT.topicName, P.contents, U.nickName, P.addressName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , P.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "           when TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "           else concat(TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "           end as uploadAt, if(ISNULL(PL.CountLike), 0, PL.CountLike) as countEmpathy,if(ISNULL(C.CountComment), 0, C.CountComment) as countComment\n" +
                "from Post P\n" +
                "         inner join PostTopic PT using(topicId)\n" +
                "         inner join User U using(userId)\n" +
                "         left outer join (select postId, count(postLikeId) CountLike from PostLike group by (postId)) as PL using(postId)\n" +
                "         left outer join (select postId, count(commentId) CountComment from Comment group by (postId)) C using(postId)\n" +
                "where P.addressName in (select addressName from Address where userId = ? and representAdd = 1) and topicName = ?";
        Object[] getPostsParams = new Object[] {userId, topicName};

        List<GetPostsRes> getPostsResList = this.jdbcTemplate.query(getPostsTopicQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postId"),
                        rs.getString("topicName"),
                        rs.getString("contents"),
                        rs.getString("nickName"),
                        rs.getString("addressName"),
                        rs.getString("uploadAt"),
                        rs.getInt("countEmpathy"),
                        rs.getInt("countComment")), getPostsParams);

        String getPostIdQuery = "select postId from Post inner join PostTopic using(topicId) where addressName in (select addressName from Address where userId = ? and representAdd = 1) and topicName = ?";

        List<Integer> postIdList = this.jdbcTemplate.query(getPostIdQuery,
                (rs, rowNum) -> new Integer(rs.getInt("postId")), getPostsParams);

        String getImgListQuery = "select ImgUrl from PostImg where postId = ? order by (postImgId) limit 3";

        for (int i = 0; i < postIdList.size(); i++) {
            List<String> imgUrlList = this.jdbcTemplate.query(getImgListQuery,
                    (rs, rowNum) -> new String(rs.getString("ImgUrl")) ,postIdList.get(i));

            getPostsResList.get(i).setImgUrlList(imgUrlList);
        }

        return getPostsResList;
    }

    public int checkPost(int postId) {
        String checkPostQuery = "select exists(select postId from Post where postId = ?)";
        int checkPostParam = postId;

        return this.jdbcTemplate.queryForObject(checkPostQuery, int.class, checkPostParam);
    }

    public GetPostRes getPost(int userId, int postId) {
        String getPostQuery = "select PT.topicName, P.userId, U.profileImgUrl, U.nickName, A.addressName, A.certifyTime,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, P.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , P.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "           when TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , P.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "           else concat(TIMESTAMPDIFF(SECOND, P.createAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "           end as uploadAt, P.contents, if(ISNULL(PL.CountLike), 0, PL.CountLike) as countEmpathy,if(ISNULL(C.CountComment), 0, C.CountComment) as countComment\n" +
                "from Post P\n" +
                "         inner join PostTopic PT using(topicId)\n" +
                "         inner join User U using (userId)\n" +
                "         inner join (select userId, addressName, certifyTime from Address where representAdd = 1) A using(userId)\n" +
                "         left outer join (select postId, count(postLikeId) CountLike from PostLike group by (postId)) as PL using(postId)\n" +
                "         left outer join (select postId, count(commentId) CountComment from Comment group by (postId)) C using(postId)\n" +
                "where postId = ?";
        int getPostParam = postId;

        GetPostRes getPostRes = this.jdbcTemplate.queryForObject(getPostQuery, (rs, rowNum) -> new GetPostRes(
                rs.getString("topicName"),
                rs.getInt("userId"),
                rs.getString("profileImgUrl"),
                rs.getString("nickName"),
                rs.getString("addressName"),
                rs.getInt("certifyTime"),
                rs.getString("uploadAt"),
                rs.getString("contents"),
                rs.getInt("countEmpathy"),
                rs.getInt("countComment")), getPostParam);

        String getImgUrlQuery = "select ImgUrl from PostImg where postId = ?";
        List<String> imgUrlList = this.jdbcTemplate.query(getImgUrlQuery,
                (rs, rowNum) -> new String(rs.getString("ImgUrl")), getPostParam);
        getPostRes.setImgUrlList(imgUrlList);

        String getCommentStructQuery = "select commentId, profileImgUrl, nickName, addressName, case\n" +
                "                                                            when TIMESTAMPDIFF(DAY, C.createAt, CURRENT_TIMESTAMP) >= 1 then concat(TIMESTAMPDIFF(DAY, C.createAt, CURRENT_TIMESTAMP), '일 전')\n" +
                "                                                            when TIMESTAMPDIFF(MINUTE , C.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(hour , C.createAt, CURRENT_TIMESTAMP), '시간 전')\n" +
                "                                                            when TIMESTAMPDIFF(SECOND, C.createAt, CURRENT_TIMESTAMP) >= 60 then concat(TIMESTAMPDIFF(MINUTE , C.createAt, CURRENT_TIMESTAMP), '분 전')\n" +
                "                                                            else concat(TIMESTAMPDIFF(SECOND, C.createAt, CURRENT_TIMESTAMP), '초 전')\n" +
                "    end as createAt, C.contents, parentIdx, if(isNull(CL.countLikes), 0, CL.countLikes) as countLikes, ImgUrl\n" +
                "from Comment C\n" +
                "         left outer join CommentImg using (commentId)\n" +
                "         left outer join (select commentId, COUNT(commentLikeId) as countLikes from CommentLike group by (commentId)) CL using (commentId)\n" +
                "         inner join User using (userId)\n" +
                "         inner join (select userId, addressName from Address where representAdd = 1) A using (userId)\n" +
                "where postId = ? order by (commentId)";

        List<CommentStruct> commentStructList = this.jdbcTemplate.query(getCommentStructQuery,
                (rs, rowNum) -> new CommentStruct(
                        rs.getInt("commentId"),
                        rs.getString("profileImgUrl"),
                        rs.getString("nickName"),
                        rs.getString("addressName"),
                        rs.getString("createAt"),
                        rs.getString("contents"),
                        rs.getInt("parentIdx"),
                        rs.getInt("countLikes"),
                        rs.getString("ImgUrl")), getPostParam);

        getPostRes.setCommentStructList(commentStructList);
        return getPostRes;
    }
}
