# COMP3910
**Timesheet System API Documentation**


## Deployed Links
Yogesh Kumar Verma http://asst3-yverma-assignments.apps.okd4.infoteach.ca/api \
Sung Na http://comp-3910-assignment-3-git-nas-assignments.apps.okd4.infoteach.ca/ 


**Timesheets**

<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URI</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td><strong>/</strong>timesheets
   </td>
   <td><strong>Employee login</strong>
<p>
Gets the timesheets of the authenticated employee
<p>
<strong>Admin login</strong>
<p>
Gets the timesheets of all employees
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/timesheets/:timesheetId
   </td>
   <td>Gets the specified timesheet
   </td>
  </tr>
  <tr>
   <td>POST
   </td>
   <td>/timesheets
   </td>
   <td>Creates a new timesheet
<p>
<strong>Body</strong>
<p>
<strong><code>"details": TimesheetRow[],</code></strong>
<strong><code>"employee": Employee,</code></strong>
<strong><code>"endWeek": String (Date)</code></strong>
   </td>
  </tr>
  <tr>
   <td>PATCH
   </td>
   <td>/timesheets/:timesheetId
   </td>
   <td>Updates an existing timesheet
   </td>
  </tr>
</table>


**Timesheet Rows**

<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URI</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td><strong>/</strong>rows?timesheetId=
   </td>
   <td>Gets the timesheet rows for a particular timesheet
<p>
<strong>Required query parameter</strong> - timesheetId
   </td>
  </tr>
  <tr>
   <td>POST
   </td>
   <td>/rows?timesheetId=
   </td>
   <td>Creates a new row for a specified timesheet
<p>
<strong>Required query parameter </strong>- timesheetId - The id of the timesheet owning this timesheet row
<p>
<strong>Body</strong>
<p>
<strong><code>"hoursForWeek": BigDecimal[],</code></strong>
<strong><code>"projectID": Integer,</code></strong>
<strong><code>"notes": String,</code></strong>
<strong><code>"workPackage": "12345"</code></strong>
   </td>
  </tr>
  <tr>
   <td>PATCH
   </td>
   <td>/rows?timesheetId=
   </td>
   <td>Updates an existing timesheet row 
<p>
<strong>Body</strong>
<p>
<strong><code>"hoursForWeek": BigDecimal[],</code></strong>
<strong><code>"id": Integer,</code></strong>
<strong><code>"projectID": Integer,</code></strong>
<strong><code>"notes": String,</code></strong>
<strong><code>"workPackage": "12345"</code></strong>
   </td>
  </tr>
</table>


**Employees**

<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URL</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/employees
   </td>
   <td>Gets all employees information.
<p>
<strong>Note</strong>: This is a restricted route only accessible by the admins.
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/employees/:empId
   </td>
   <td>Gets the information of the specific employee
   </td>
  </tr>
  <tr>
   <td>POST
   </td>
   <td>/employees
   </td>
   <td>Creates a new employee with a new employee Id
<p>
<strong>Body</strong>
<p>
<strong><code>       "empNumber": Integer,</code></strong>
<strong><code>       "fullName": String,</code></strong>
<strong><code>       "username": String</code></strong>
   </td>
  </tr>
  <tr>
   <td>PATCH
   </td>
   <td>/employees/:empId
   </td>
   <td>Updates an existing employee information
   </td>
  </tr>
  <tr>
   <td>DELETE
   </td>
   <td>/employees/:empId
   </td>
   <td>Deletes an existing employee
   </td>
  </tr>
</table>


**Admins**


<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URL</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/admins
   </td>
   <td>Gets all admins information.
<p>
<strong>Note</strong>: This is a restricted route only accessible by the admins.
   </td>
  </tr>
</table>


**Credentials**


<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URL</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/credentials
   </td>
   <td>Gets credentials of all employees.
<p>
<strong>Note</strong>: This is a restricted route only accessible by the admins.
   </td>
  </tr>
  <tr>
   <td>GET
   </td>
   <td>/credentials/:empUsername
   </td>
   <td>Gets the information of the specific employee
   </td>
  </tr>
  <tr>
   <td>POST
   </td>
   <td>/credentials
   </td>
   <td>Creates a credential for an employee
<p>
<strong>Body</strong>
<p>
<strong><code>   "username": String,</code></strong>
<strong><code>   "password": String,</code></strong>
<strong><code>   "empNumber": Integer (An employee with this number must exist)</code></strong>
   </td>
  </tr>
  <tr>
   <td>PATCH
   </td>
   <td>/credentials/:empUsername
   </td>
   <td>Updates an existing credential
   </td>
  </tr>
  <tr>
   <td>DELETE
   </td>
   <td>/credentials/:empUsername
   </td>
   <td>Deletes an existing credential
   </td>
  </tr>
</table>


**Authentication**


<table>
  <tr>
   <td><strong>HTTP Verb</strong>
   </td>
   <td><strong>URL</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>POST
   </td>
   <td>/authentication
   </td>
   <td>Authenticates a user and provides a token
<p>
<strong>Body</strong>
<p>
<strong><code>   "username": "sna",</code></strong>
<strong><code>   "password": "1234567890"</code></strong>
   </td>
  </tr>
</table>

## How to use ?
1. In Postman, create a POST request to the URL: /authentication
2. The credentials to be used for authentication can be gotten by creating an issue in this repository
and entered in the request as follows:

    ```
    {
       "username": username",
       "password": "password"
    }
    ```


3. Store the token received, this token will be used for authentication and authorization in all other requests
4. Choose any of the URI’s in the documentation to make a request to
5. After creating that request, navigate to the authorization tab in Postman
6. Choose Bearer token as means of authorization
7. Paste the bearer token into the input field
8. Repeat this for every new request that is create

## Contributors
Sung Na\
Yogesh Kumar Verma
