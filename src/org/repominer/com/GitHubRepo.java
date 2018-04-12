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
	public ArrayList<Contributer> contributers = new ArrayList<Contributer>();
	public Integer linesDeleted = 0;
	public Integer linesAdded = 0;
	public GitHubRepo(String url) {
		String githubPrefix = "https://github.com/";
		url = url.replace("https://github.com/", "");
		url = url.replace("https://www.github.com/", "");
		this.repoName = url.replace("/", "_");
		String folderPath = File.separatorChar + "RepoMiner" + File.separatorChar + "repos" + File.separatorChar + this.repoName;
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
	private Contributer getContributerFromEmail(String email) {
		for (Contributer contributer: this.contributers) {
			if (contributer.getAuthorEmail().equals(email)) {
				return contributer;
			}
		}
		return null;	
	}
	public boolean analyse() {
		boolean success = true;
		try {
			String s = null;
			Process p = Runtime.getRuntime().exec("git --git-dir " + this.repoPath.concat(File.separatorChar + ".git") + " log --pretty=format:'>>>>%h,%an,%aE,%at' --numstat --all");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			System.out.println(String.format("Analysing %s", this.url));
			while (!stdInput.ready() && p.isAlive()) {
				//wait
			}
			Contributer tempContributer = null;
			Commit tempCommit = null;
			Integer lineNum = 0;
			while ((s = stdInput.readLine()) != null) {
				lineNum++;
				String tempLine = s.replace("'", "");
				if (tempLine.length() == 0) {
					continue;
				}
				if (tempLine.startsWith(">>>>")) {
					tempLine = tempLine.replace(">>>>", "");
	            	String[] parts = tempLine.split(",");
	            	tempCommit = new Commit(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
	            	this.commits.add(tempCommit);
	            	if ((tempContributer = this.getContributerFromEmail(parts[2])) != null) {
	            		tempContributer.addCommit(tempCommit);
	            	}
	            	else {
	            		tempContributer = new Contributer(parts[1], parts[2]);
	            		tempContributer.addCommit(tempCommit);
	            		this.contributers.add(tempContributer);
	            	}
				}
				else {
					String[] parts = tempLine.split("\t");
					try {
						tempCommit.addLinesAdded(Integer.parseInt(parts[0]));
						tempCommit.addLinesDeleted(Integer.parseInt(parts[1]));
						this.linesAdded += Integer.parseInt(parts[0]);
						this.linesDeleted += Integer.parseInt(parts[1]);
					}
					catch (NumberFormatException e) {
						System.out.println(String.format("Could not parse line %s of repository %s:%s", lineNum, this.repoName,tempLine));
					}
				}
            }
			File file = new File(System.getProperty("user.home") + File.separatorChar + "RepoMiner" + File.separatorChar + "analysis" + File.separatorChar + this.repoName + ".csv");
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println("authorEmail,numCommits,percentageCommits,numLinesAdded,percentageLinesAdded,numLinesDeleted,percentageLinesDeleted");
			System.out.println(String.format("Lines Added:%s\nLines Deleted:%s\n", this.linesAdded, this.linesDeleted));
			for (Contributer contributer : this.contributers) {
				Integer contributerCommits = contributer.commits.size();
				float percentageCommits = contributerCommits/this.commits.size();
				Integer numLinesAdded = contributer.getLinesAdded();
				float percentageLinesAdded = 0;
				float percentageLinesDeleted = 0;
				Integer numLinesDeleted = contributer.getLinesDeleted();
				if (this.linesAdded > 0 && numLinesAdded > 0) {
					percentageLinesAdded = calculatePercentage(numLinesAdded, this.linesAdded);
				}
				if (this.linesDeleted > 0 && numLinesDeleted > 0) {
					percentageLinesDeleted = calculatePercentage(numLinesDeleted, this.linesDeleted);
				}
				System.out.println(String.format(java.util.Locale.US, "Commiter:%s\nLinesAdded:%s\nLinesDeleted:%s\npercentageDeleted:%.2f\npercentageAdded:%.2f", contributer.getAuthorEmail(), numLinesAdded, numLinesDeleted, percentageLinesDeleted, percentageLinesAdded));
				writer.println(String.format(java.util.Locale.US, "%s,%s,%.2f,%s,%.2f,%s,%.2f", contributer.getAuthorEmail(), contributerCommits, percentageCommits, numLinesAdded, percentageLinesAdded, numLinesDeleted, percentageLinesDeleted));
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
	private Float calculatePercentage(int n, int d) {
		return (n * 1f) / d;
	}
	
}
