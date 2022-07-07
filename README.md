## MVP code challenge #2 - The Rat Maze

#### additional notes:
for sake of simplicity and readability `POST /v1/maze/serial`
end point exist solely for parsing serialized mazes from string

(i.e):

post /v1/maze/serial

```
#######E##
##       #
###  # ###
### ######
```

request body should be a valid serialized maze


what is a valid maze?:
* it has only one entrance point noted with `E` in serialized form
* it has only one exit point in outer layer of the maze
* entrance point and exist point are not at the edges
* all the rows have exactly the same size


### serialization token table 

| tile type | char | char name |
|:---------:|:----:|:---------:|
|   wall    | `W`  | capital w |
| entrance  | `E`  | capital e |
|   space   | ` `  |   space   |


