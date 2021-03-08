# REST API test application

## Installation

1. Create and Run postgres docker container:

```sh
docker run --name some-postgres --publish 127.0.0.1:5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
2. Clone this repo.
3. Create schemas and populate test tables:
```sh
sbt run blog.utils.CreateTables
sbt 'test:runMain blog.PopulateTables'
```
4. sbt run Server class

## Testing REST API

Using browser :

1. ```GET /blog/?key=<asc|desc>&order=<createdat|editedat>``` - to show all posts, key and order to sort the result
2. ```GET /blog/:id``` - get post at specific id

With CURL :

3. ```POST /blog``` - add a new blog entry, JSON format

example:

```sh
curl -H 'Content-Type: application/json' -s -XPOST http://127.0.0.1:8080/blog -d '{"title": "book", "content": "content", "tags": ["a", "b"], "author": "someone"}'
```
4. ```PUT /blog:id``` - update blog with entry

example:

```sh
curl -H 'Content-Type: application/json' -s -XPUT http://127.0.0.1:8080/blog/94 -d '{"title": "bookNew", "content": "contentNew", "tags": ["a", "c"], "author":"somenew"}'
```

5. ```DELETE /blog:id``` - deletes a blog entry by an id (cannot if there are duplicate id's present)
