package org.repominer.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class GitHubRepo {
	private String url = "";
	private String repoName = "";
	private String repoPath = "";
	public ArrayList<Commit> commits = new ArrayList<Commit>();
	public HashMap<String, Integer> contributions = new HashMap<String, Integer>();
	public Integer linesDeleted = 0;
	public Integer linesAdded = 0;
	public GitHubRepo(String url) {
		String githubPrefix = "https://github.com/";
		url = url.replace("https://github.com/", "");
		url = url.replace("https://www.github.com/", "");
		this.repoName = url.replace("/", "_");
		String folderPath = File.separatorChar + "Documents" + File.separatorChar + "RepoMiner" + File.separatorChar + "repos" + File.separatorChar + this.repoName;
		this.repoPath = System.getProperty("user.home").concat(String.format(folderPath));
		this.url = githubPrefix.concat(url);
	}
	public boolean cloneProject() {
		boolean success = true;
		try {
			
			String s = null;
			Process p = Runtime.getRuntime().exec(String.format("git clone %s %s", this.url, this.repoPath));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			System.out.println(String.format("Cloning %s", this.url));
			while (!stdInput.ready() && p.isAlive()) {
				//wait
			}
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                success = false;
            }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	public boolean analyse() {
		boolean success = true;
		try {
			String s = null;
			Process p = Runtime.getRuntime().exec("git --git-dir " + this.repoPath.concat(File.separatorChar + ".git") + " log --pretty=format:'>>>>%h,%an,%aE,%at'");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			System.out.println(String.format("Cloning %s", this.url));
			while (!stdInput.ready() && p.isAlive()) {
				//wait
			}
			Commit tempCommit = null;
			while ((s = stdInput.readLine()) != null) {
				String tempLine = s.replace("'", "");
				if (tempLine.startsWith(">>>>")) {
	            	String[] parts = tempLine.split(",");
	            	tempCommit = new Commit(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
	            	if (!contributions.containsKey(parts[2])) {
	            		contributions.put(parts[2], 1);
	            	}
	            	else {
	            		contributions.put(parts[2], contributions.get(parts[2])+1);
	            	}
				}
				else {
					String[] parts = tempLine.split("\t");
					tempCommit.setLinesAdded(Integer.parseInt(parts[0]));
					tempCommit.setLinesDeleted(Integer.parseInt(parts[1]));
					this.linesAdded += tempCommit.getLinesAdded();
					this.linesDeleted += tempCommit.getLinesDeleted();
				}
            	this.commits.add(tempCommit);
            }
			File file = new File(System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "RepoMiner" + File.separatorChar + "analysis" + File.separatorChar + this.repoName + ".csv");
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println("authorEmail,numCommits,percentageCommits,numLinesAdded,percentageLinesAdded,numLinesDeleted,percentageLinesDeleted");
			for (String key : this.contributions.keySet()) {
				String contributerEmail = key;
				Integer contributerCommits = this.contributions.get(key);
				Integer percentageCommits = contributerCommits/this.commits.size();
				Integer numLinesAdded = this.getNumLinesAddedForAuthor(key);
				Integer percentageLinesAdded = 0;
				Integer percentageLinesDeleted = 0;
				Integer numLinesDeleted = this.getNumLinesDeletedForAuthor(key);
				if (this.linesAdded != 0 || numLinesAdded != 0) {
					percentageLinesAdded = Math.abs(numLinesAdded/this.linesAdded);
				}
				if (this.linesDeleted != 0 || numLinesDeleted != 0) {
					percentageLinesDeleted = Math.abs(numLinesDeleted/this.linesDeleted);
				}
				writer.println(String.format("%s,%s,%s,%s,%s,%s", contributerEmail, contributerCommits, percentageCommits, numLinesAdded, percentageLinesAdded, numLinesDeleted, percentageLinesDeleted));
			}
			writer.close();
			if (!stdError.toString().equals("")) {
				System.out.println("Here is the standard error of the command (if any):\n");
	            while ((s = stdError.readLine()) != null) {
	                System.out.println(s);
	                success = false;
	            }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	private Integer getNumLinesAddedForAuthor(String key) {
		Integer authorLinesAdded = 0;
		for (Commit commit: this.commits) {
			if (commit.getAuthorEmail().equals(key)) {
				authorLinesAdded += commit.getLinesAdded();
			}
		}
		return authorLinesAdded;
	}
	private Integer getNumLinesDeletedForAuthor(String key) {
		Integer authorLinesDeleted = 0;
		for (Commit commit: this.commits) {
			if (commit.getAuthorEmail().equals(key)) {
				authorLinesDeleted += commit.getLinesDeleted();
			}
		}
		return authorLinesDeleted;
	}
}
