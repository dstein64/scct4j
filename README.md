scct4j
======

Keywords: crud, java, angular, derby, rest

*scct4j* stands for *Self-Contained CRUD Template for Java*. It is a basic RESTish
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) webapp.
Support for file uploads is included by the use of a multipart/form-data API.

It is not intended to be useful as-is, but rather it is a template that can be
used to build more extensive CRUD webapps. It is implemented using AngularJS for
the UI, Java for the server, and Derby for its database. The only dependency is Java.
Otherwise, the app is self-contained.

How To Use
----------

To use scct4j, the code has to be compiled and the database initialized.
Then the server can be launched.

```
git clone 'https://github.com/dstein64/scct4j.git'
cd scct4j
./build.sh
./run.sh datarepo.DatabaseManager init
./run.sh datarepo.server.Main 8000
```

The example above uses port 8000. To use the webapp, navigate your browser
to http://localhost:8000/.

Screenshots
-----------

The following images show various components of scct4j's UI.

License
-------

See [LICENSE](LICENSE).
This license does not apply to the libraries. For the library licenses, see their corresponding licenses.
