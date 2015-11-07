scct4j
======

Keywords: crud, java, angular, bootstrap, derby, rest

*scct4j* stands for *Self-Contained CRUD Template for Java*. It is a RESTish
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) webapp.
Support for file uploads is included by the use of a multipart/form-data API.

It is not particularly useful as-is, but rather it is a template that can be
used to build more extensive CRUD webapps. It is implemented using AngularJS and
Bootstrap for the UI, Java for the server (using embedded Tomcat), and Derby for
its embedded database. The only dependency is Java. Otherwise, the app is self-contained.

How To Use
----------

To use scct4j, the code has to be compiled and the database initialized.
Then the server can be launched.

```
git clone 'https://github.com/dstein64/scct4j.git'
cd scct4j
./build.sh
./run.sh scct4j.DatabaseManager init
./run.sh scct4j.server.Main 8000
```

The example above uses port 8000. To use scct4j, navigate your browser
to [http://localhost:8000/](http://localhost:8000/).

Screenshots
-----------

The following images show various components of scct4j's UI.

License
-------

scct4j has an [MIT License](https://en.wikipedia.org/wiki/MIT_License).

See [LICENSE](LICENSE).

This license does not apply to the libraries. For the library licenses, see their corresponding licenses.
