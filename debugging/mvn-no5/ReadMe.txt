In this test case, you have to fix the bug which is described in the test case. (2 day)

Prerequisites:
* Install maven eclipse and run-jetty-run, please refer to this guide - http://books.zkoss.org/wiki/ZK_Installation_Guide/Setting_up_IDE/Maven/Setting_up_Maven_on_IDE/Setting_up_Maven_on_Eclipse
* JDK 1.5 later
* Import the mvn-no1 folder as a Eclipse project.

After you ready, please visit the page http://localhost:8080/mvn-no5/
and then you will see some description on the page as follows.

Queston:
1. Click on "Resize" Button. The number after "Page Size:" should change. Otherwise it is a bug.

Note: you can check out the ZK and Zul source code to debug this issue.
* ZK: https://zk1.svn.sourceforge.net/svnroot/zk1/releases/5.0.6/zk/
* Zul: https://zk1.svn.sourceforge.net/svnroot/zk1/releases/5.0.6/zul/