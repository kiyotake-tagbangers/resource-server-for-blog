# resource server for blog

## Sequence diagram

![Sequence diagram](./images/AuthorizationCodeFlowForBlog.png)

## Run locally

- run keycloak

```shell
$ docker-compose up -d
```

- run resource server

```shell
$ export JAVA_HOME=`/usr/libexec/java_home -v 11`

$ java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)

$ ./mvnw clean package
$ ./mvnw spring-boot:run
```

- access keycloak master realm
    - [http://localhost:8000/](http://localhost:8000/)
        - username and password are "keycloak" as written in docker-compose

- copy sample realm client secret for using following process

- access via a browser for getting authorization code
    - username is "sample"
    - password is "1qazxsw2"
    - response uri contains authorization code (code=abcde...)

```
http://localhost:8000/auth/realms/sample/protocol/openid-connect/auth?client_id=sample&response_type=code&scope=openid profile&redirect_uri=http://localhost:8083/callback&state=YQyUWX4wxzxE
```

- exchange authorization code for access token

```shell
### client_secret use you confirmed
### code use you got
$ curl --location --request POST 'http://localhost:8000/auth/realms/sample/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=sample' \
--data-urlencode 'client_secret=f6aca15e-3dbc-475d-8a5e-547cff463d24' \
--data-urlencode 'code=aece2ccb-b059-4ea8-90f1-2809c25a5221.447a75ae-c922-47a3-8f26-d93af41c1fa2.35bad630-d5b8-4472-90e5-15a97d354177' \
--data-urlencode 'redirect_uri=http://localhost:8083/callback' \
--data-urlencode 'scope=openid profile'
```

- copy "acess_token" section  for using following process

```shell
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1clFyczhPSDZzOU9rUDlMWmdfMk1FZmxYZGpQRFI0aENjVkhNWk5pbHNVIn0.eyJleHAiOjE2MDQ0NjA3NTgsImlhdCI6MTYwNDQ2MDQ1OCwiYXV0aF90aW1lIjoxNjA0NDYwMzIwLCJqdGkiOiIzYTE3YWZmYS0xMmFmLTQ2MjMtOTkzNC02Mjg4ZDViYTY2MDIiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwMDAvYXV0aC9yZWFsbXMvc2FtcGxlIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImUxYTBmMzkwLTMzZGMtNDNjOS04NTNlLTQyOTUwNGNmY2E4YSIsInR5cCI6IkJlYXJlciIsImF6cCI6InNhbXBsZSIsInNlc3Npb25fc3RhdGUiOiI0NDdhNzVhZS1jOTIyLTQ3YTMtOGYyNi1kOTNhZjQxYzFmYTIiLCJhY3IiOiIwIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzYW1wbGUifQ.ii0O8IV1DK3lMF9VAXrt_l3QwVGjQdYCmmUPxT7HlmkPGy8KZzVnYawZ5bm4gAZ733NpgGUT4goM9cL-ZvoqaIvey8M_KP2CH9mcBP1gNFdPapzDzWJ4HjvVRR42foAZvSCVQbhKeUYAUAU0LyewxYnNtYlWnxNyL9zfP5icFSbd7M8vTvEzQ1gwDiBG6wM-6_8JrPcYJjU6X7NG9mCM4oIQ1Fc5NhyBUJvnjDv_6PMCF2ZzJTQZMn1pieGgntBntXlLe1GEr1JvOmsztyJXF2ytrBit52ZImq4yW9l2HZE4BhckZt9PQ6K6xVvvyx6uK0rpTzEaWraxnoemwwoTHA"
```

- get resource from resource server
    - use Bearer token you got previous step

```shell
$ curl --location --request GET 'http://localhost:8081/status/check/' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ1clFyczhPSDZzOU9rUDlMWmdfMk1FZmxYZGpQRFI0aENjVkhNWk5pbHNVIn0.eyJleHAiOjE2MDQ0NjA3NTgsImlhdCI6MTYwNDQ2MDQ1OCwiYXV0aF90aW1lIjoxNjA0NDYwMzIwLCJqdGkiOiIzYTE3YWZmYS0xMmFmLTQ2MjMtOTkzNC02Mjg4ZDViYTY2MDIiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwMDAvYXV0aC9yZWFsbXMvc2FtcGxlIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImUxYTBmMzkwLTMzZGMtNDNjOS04NTNlLTQyOTUwNGNmY2E4YSIsInR5cCI6IkJlYXJlciIsImF6cCI6InNhbXBsZSIsInNlc3Npb25fc3RhdGUiOiI0NDdhNzVhZS1jOTIyLTQ3YTMtOGYyNi1kOTNhZjQxYzFmYTIiLCJhY3IiOiIwIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzYW1wbGUifQ.ii0O8IV1DK3lMF9VAXrt_l3QwVGjQdYCmmUPxT7HlmkPGy8KZzVnYawZ5bm4gAZ733NpgGUT4goM9cL-ZvoqaIvey8M_KP2CH9mcBP1gNFdPapzDzWJ4HjvVRR42foAZvSCVQbhKeUYAUAU0LyewxYnNtYlWnxNyL9zfP5icFSbd7M8vTvEzQ1gwDiBG6wM-6_8JrPcYJjU6X7NG9mCM4oIQ1Fc5NhyBUJvnjDv_6PMCF2ZzJTQZMn1pieGgntBntXlLe1GEr1JvOmsztyJXF2ytrBit52ZImq4yW9l2HZE4BhckZt9PQ6K6xVvvyx6uK0rpTzEaWraxnoemwwoTHA'
```

- response from resource server

```shell
working...
```