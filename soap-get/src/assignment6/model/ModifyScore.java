package assignment6.model;

public class ModifyScore {
    private String sid;
    private String cid;
    private String score;
    private String scoreType;

    public ModifyScore() {
    }

    public ModifyScore(String sid, String cid, String score, String scoreType) {
        this.sid = sid;
        this.cid = cid;
        this.score = score;
        this.scoreType = scoreType;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }
}
