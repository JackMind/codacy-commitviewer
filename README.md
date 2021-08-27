# Developer exercise - Commit Viewer

Commit Viewer is a Spring Boot app that returns a list of commits from a provided valid git hub url.
The API provides a pagination mechanism.

## How to run it

    mvn spring-boot:run

## Try the API
To try the API, you can simply use the built-in swagger ui, by accessing:

    http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/commit-controller

# Detailed view

The main workflow of this app is to execute a request to the GitHub API via 
HTTP Rest to a specified remote git hub url. In case of this request fails, 
a "local" execution is tried in order to obtain the list of commits.

## Application architecture
For the application architecture it was a used a 3 layer architecture with a DDD paradigm.
Where the main focus was to isolate the domain logic from the external interfaces, API's, and infrastructures.

## Remote execution
The remote execution, is a simple HTTP Rest request to the GitHub API
(https://docs.github.com/en/rest/reference/repos#commits)

## Local Execution
The local execution, happens when the remote execution fails or if 
it is specified on the api.
The approach is as follows:
1. A local temporary directory is created at temp file system of host;
2. The temporary directory, has a prefix containing the owner and repository
name of the given github url;
3. Command git clone with depth flag is executed, cloning only the page size
   size specified on the request;
4. The path to the temporary directory is saved on cache (key - github url, value - path);
5. If the page number is greater than 1, it means that we need to pull more history
    from the remote repo, so a git fetch command with a depth flag is executed to
    pull the required extra commit history;
6. Git log command is executed with specified flags, so the output is easier to parse.

### Cache
The cache is intended to keep track of the existing repositories, as well as, taking
advantage of some features given by the Cacheable API, to set expire times and
callbacks on those expire occur, that way we can easily delete the local directories and manage
hard disk space.

## Future work

* Add functionality to select which branch to request the commits from.