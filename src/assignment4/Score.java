package assignment4;

import java.util.ArrayList;

public class Score {
	private ArrayList<SingleScore> scoreList;
	private String courseId;
	private String scoreType;
	
	public ArrayList<SingleScore> getScoreList() {
		return scoreList;
	}
	public void setScoreList(ArrayList<SingleScore> scoreList) {
		this.scoreList = scoreList;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getScoreType() {
		return scoreType;
	}
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}
	
	
	
}
