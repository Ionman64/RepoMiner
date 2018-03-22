package org.repominer.com;

public class Commit {
	private String commitHash = null;
	private String authorName = null;
	private String authorEmail = null;
	private int date;
	private Integer linesAdded = 0;
	private Integer linesDeleted = 0;
	public Commit(String commitHash, String authorName, String authorEmail, int date) {
		this.commitHash = commitHash;
		this.authorName = authorName;
		this.authorEmail = authorEmail;
		this.date = date;
	}
	public String getCommitHash() {
		return commitHash;
	}
	public String getAuthorName() {
		return authorName;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public int getDate() {
		return date;
	}
	public Integer getLinesAdded() {
		return linesAdded;
	}
	public Integer getLinesDeleted() {
		return linesDeleted;
	}
	public void addLinesDeleted(Integer linesDeleted) {
		this.linesDeleted += linesDeleted;
	}
	public void addLinesAdded(Integer linesAdded) {
		this.linesAdded += linesAdded;
	}
}
