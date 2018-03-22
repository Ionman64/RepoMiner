package org.repominer.com;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		//Setup[] 
		String userHome = System.getProperty("user.home");
		new File(userHome.concat("RepoMiner")).mkdir();
		new File(userHome.concat("RepoMiner" + File.separatorChar + "repos")).mkdir();
		new File(userHome.concat("RepoMiner" + File.separatorChar + "analysis")).mkdir();
		System.out.println("Starting To Mine Github");
		GitHubRepo githubrepo = new GitHubRepo("https://github.com/ionman64/CalendarView");
		githubrepo.cloneProject();
		githubrepo.analyse();
		
	}
}
