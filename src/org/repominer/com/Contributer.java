package org.repominer.com;

import java.util.ArrayList;

public class Contributer {
	private String authorName = null;
	private String authorEmail = null;
	public ArrayList<Commit> commits = new ArrayList<Commit>();
	public Contributer(String authorName, String authorEmail) {
		this.authorName = authorName;
		this.authorEmail = authorEmail;
	}
	public String getAuthorName() {
		return authorName;
	}
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void addCommit(Commit commit) {
		this.commits.add(commit);
	}
	public Integer getLinesAdded() {
		Integer authorLinesAdded = 0;
		for (Commit commit: this.commits) {
			authorLinesAdded += commit.getLinesAdded();
		}
		return authorLinesAdded;
	}
	public Integer getLinesDeleted() {
		Integer authorLinesDeleted = 0;
		for (Commit commit: this.commits) {
			authorLinesDeleted += commit.getLinesDeleted();
		}
		return authorLinesDeleted;
	}
}
