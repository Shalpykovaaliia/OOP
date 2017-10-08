<h1 align="center">LIbrary Management System  for JARS </h1>



Features
-------------------
  * Manage Borrower
    - add
      * allow using webcam to capture profile picture
    - delete 
    - edit
    - list borrowers

  * Manage book record
    - add 
    - view
      * show status of book (borrowed or avaibale)
        if available
      * Show barcode output
      * Print barcode 
    - list books
      * show book information and availability
    - edit
    - delete
    - borrow
    - search and filter
    - audit 
      * show history of who borrowed what books
  * Notify user when the book is due
    - allow setting of time of the day
  * Notify admin when credits run out 

  * System user registration form

  * Registration form for end user

  * Application Settings
      dynamic setting (
        Number of allowed books to borrow Penalty, 
        number of allowable days
  * Scan barcode 
    - of books and show book information
    - of borrower/library personel id and show person information
  * Penalty Information
  * Returned Book monitor
  * Borrowed Books monitoring
  * Penalty Report by date range
    - show list of people that paid penalty and show its total collected
  * Print barcode image
  * Show list of borrowed books



DIRECTORY STRUCTURE
-------------------

      assets/             contains assets definition
      commands/           contains console commands (controllers)
      config/             contains application configurations
      controllers/        contains Web controller classes
      mail/               contains view files for e-mails
      models/             contains model classes
      runtime/            contains files generated during runtime
      tests/              contains various tests for the basic application
      vendor/             contains dependent 3rd-party packages
      views/              contains view files for the Web application
      web/                contains the entry script and Web resources



REQUIREMENTS
------------

  * The minimum requirement by this project template that your Web server supports PHP 5.4.0.
  * php is added in path location.


INSTALLATION
------------

### Install from an Archive File

1.) Extract the archive compressed file to desired location or any location
1.) Download https://getcomposer.org/download/1.5.2/composer.phar
1.) Place the download composer.phar to the extracted folder. Paste the composer.phar next to start.bat. 
1.) Double click install.bat




You can then access the application through the following URL:

~~~
http://localhost:8080
~~~


CONFIGURATION
-------------

### Database

Edit the file `config/db.php` with real data, for example:

```php
return [
    'class' => 'yii\db\Connection',
    'dsn' => 'mysql:host=localhost;dbname=yii2basic',
    'username' => 'root',
    'password' => '1234',
    'charset' => 'utf8',
];
```

**NOTES:**
- It won't create the database for you, this has to be done manually before you can access it.
- Check and edit the other files in the `config/` directory to customize your application as required.


TESTING
-------

Tests are located in `tests` directory. They are developed with [Codeception PHP Testing Framework](http://codeception.com/).
By default there are 3 test suites:

- `unit`
- `functional`
- `acceptance`

Tests can be executed by running

```
vendor/bin/codecept run
```

The command above will execute unit and functional tests. Unit tests are testing the system components, while functional
tests are for testing user interaction. Acceptance tests are disabled by default as they require additional setup since
they perform testing in real browser. 
