
Various parameters that can be changed are in SomeTestTasks\src\main\resources\config.properties 

Several txt files used for testing upload/download process are in src\main\resources\data\ 

The project generates emailable-report.html at: SomeTestTasks\target\surefire-reports\
and separate log files at: eis-test-task\target\logs for different tresholds (debug and info)

Maven profiles are: 
testall (default) - runs all tests 
testdriveapi - runs only GoogleDrive API upload/download tests
testfilesfm - runs only FIlesFm Selenium upload/download tests
testftp - runs only Ftp server test 
testonliner - runs only the test for Onliner




