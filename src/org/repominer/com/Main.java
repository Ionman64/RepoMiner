package org.repominer.com;

import java.io.File;
import java.io.IOException;

import org.repominer.visualiser.*;

import com.repominer.server.Server;

public class Main {

	public static void main(String[] args) throws IOException {
		String accessToken = "c0506854272cca9f5c0680e2187e55c81ccfcfb2";
		//Setup[] 
		System.out.println(PieChartGenerate.generate());
		String userHome = System.getProperty("user.home");
		new File(userHome.concat(File.separatorChar + "RepoMiner")).mkdir();
		new File(userHome.concat(File.separatorChar + "RepoMiner" + File.separatorChar + "repos")).mkdir();
		new File(userHome.concat(File.separatorChar +"RepoMiner" + File.separatorChar + "analysis")).mkdir();
		System.out.println("Starting To Mine Github");
		GitHubRepo githubrepo = new GitHubRepo("https://github.com/jHelsing/Heartbeat");
		
		//APIHandler apiHandler = new APIHandler(accessToken, "joshuaju", "SEP_webclient");
		//apiHandler.getCommits();
		githubrepo.cloneProject();
		githubrepo.analyse();
		Server server = new Server();
		
	}
}
