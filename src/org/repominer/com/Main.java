package org.repominer.com;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		String accessToken = "";
		//Setup[] 
		String userHome = System.getProperty("user.home");
		new File(userHome.concat(File.separatorChar + "RepoMiner")).mkdir();
		new File(userHome.concat(File.separatorChar + "RepoMiner" + File.separatorChar + "repos")).mkdir();
		new File(userHome.concat(File.separatorChar +"RepoMiner" + File.separatorChar + "analysis")).mkdir();
		System.out.println("Starting To Mine Github");
		GitHubRepo githubrepo = new GitHubRepo("https://github.com/joshuaju/SEP_webclient");
		APIHandler apiHandler = new APIHandler(accessToken, "joshuaju", "SEP_webclient");
		apiHandler.getCommits();
		//githubrepo.cloneProject();
		//githubrepo.analyse();
		
	}
}
