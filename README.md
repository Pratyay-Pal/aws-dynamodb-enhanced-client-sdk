# aws-dynamodb-enhanced-client-adk

Uses enhanced DynamoDB Client from Java AWS SDK v2. This contains some the basic operations on DynamoDB table<br>


#### Tables that must exist
You must create a DynamoDB table named "users". Create columns with the exact name as show in the screenshots. An importable CSV file(ReadmeScreens/users.csv) is present which can be imported to DynamoDB directly to create the table and populate it with data<br>
![Image](ReadmeScreens/table.png?raw=true)
![Image](ReadmeScreens/tablecontents.png?raw=true)
<br>
<br><br>To use /userByEmail, a Global Secondary index has to be created as shown-<br>
![Image](ReadmeScreens/gsi.png?raw=true)

#### Accessing the endpoints
Hit **http://localhost:8080/endpoint** with details to do Database operations.<br><br><br>
**endpoint** can be as follows-<br><br>
/allUsers - Lists all users<br>
/allUsersWithPagination?lastUser=<user_id>&count=<numberOfUsers> - Lists numberOfUsers number of users starting from user user_id. Note the parameters<br>
/getUserDetails - Get specific user details. Requires user_id as request body<br>
/createUser - Create a new user. Requires name, email, ign as request body<br>
/deleteUser - Delete a specific user. Requires user_id as request body<br>
/userByEmail - Get specific user details. Requires email as request body<br>