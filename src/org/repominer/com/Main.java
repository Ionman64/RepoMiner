package org.repominer.com;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Main {

	public static void main(String[] args) throws IOException {
		String userHome = System.getProperty("user.home");
		new File(userHome.concat(File.separatorChar + "RepoMiner")).mkdir();
		new File(userHome.concat(File.separatorChar + "RepoMiner" + File.separatorChar + "repos")).mkdir();
		new File(userHome.concat(File.separatorChar +"RepoMiner" + File.separatorChar + "analysis")).mkdir();
		Reader in = new FileReader(userHome.concat(File.separatorChar + "RepoMiner" + File.separatorChar + "projects.csv"));
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
		for (CSVRecord record: records) {
			if (record.get(2).startsWith("https://github.com/")) {
				System.out.println("Mining " + record.get(2));
				GitHubRepo githubrepo = new GitHubRepo(record.get(2));
				githubrepo.cloneProject();
				githubrepo.gitfame();
			}
		}
	}
}
