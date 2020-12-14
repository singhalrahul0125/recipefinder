# Recipefinder
This rest api helps users find recipes by name. Users can also create an account and save their favorite recipes that they can retrieve later.

## How to configure & install
To build this Spring boot application, clone the branch to your local environment.

### Configuration

#### Database
This application requires a database if you would like to allow users to create an account and save recipes and retrieve their saved recipes. The application was built using oracle database. However, you are free to choose the database of your choice. Please update the following fields in [application.properties](https://github.com/singhalrahul0125/recipefinder/blob/master/src/main/resources/application.properties) with the details of your chosen database:

```
spring.datasource.url
spring.datasource.username
spring.datasource.password
spring.datasource.driver.class
```
#### Source API
This application uses [spoonacular API](https://spoonacular.com/food-api) to look for recipes. You will need to [sign up](https://spoonacular.com/food-api/console#Dashboard) in order to access this API. Once signed up, you will receive an api key. Please update the api key in the following field in [application.properties](https://github.com/singhalrahul0125/recipefinder/blob/master/src/main/resources/application.properties) :

```
sourceApi.apikey
```
### Installation

To install the application run the following command in the project directory:

```mvn clean install```

This command will create a `recipefinder-0.0.1-SNAPSHOT.jar`

To start the application, switch to the directory where jar file is located, and run the following command:

`java -jar recipefinder-0.0.1-SNAPSHOT.jar`

## API
### Search Recipes
Allows you to search for recipes by name. Returns a json with a list of matching recipes.

#### Request

```
GET http://{host}:{port}/recipefinder/find?recipeName={searchquery}
```
 
##### Request parameters

| Parameter        | Type           | Description  |
| :------------- |:-------------| :-----|
| recipeName      | string | The (natural language) recipe search query. For eg. pasta |

#### Sample Request and Response

##### Sample Request
```
curl --location --request GET 'http://localhost:8081/recipefinder/find?recipeName=pizza'
```

##### Sample Response
```
[
    {
        "title": "Quick and Easy St. Louis-Style Pizza",
        "sourceUrl": "https://www.cinnamonspiceandeverythingnice.com/st-louis-style-pizza/",
        "readyInMinutes": 27
    },
    {
        "title": "Breakfast Pizza",
        "sourceUrl": "http://www.jocooks.com/breakfast-2/breakfast-pizza/",
        "readyInMinutes": 25
    },
    {
        "title": "Egg & rocket pizzas",
        "sourceUrl": "https://www.bbcgoodfood.com/recipes/egg-rocket-pizzas",
        "readyInMinutes": 30
    },
    {
        "title": "Fruit Pizza",
        "sourceUrl": "https://pinchofyum.com/fruit-pizza",
        "readyInMinutes": 90
    },
    {
        "title": "Ricotta Pizza with Prosciutto and Fresh Pea Salad",
        "sourceUrl": "https://kitchenconfidante.com/ricotta-pizza-with-prosciutto-and-fresh-pea-salad-recipe",
        "readyInMinutes": 30
    }
]
```
### Create account
Allows a user to create an account. One a user is successfully created, the endpoint returns a key unique to the user. Using their unique key, the users can save recipes to their account and retrieve their saved recipes.

#### Request

```
POST http://{host}:{port}/recipefinder/account
```
 
##### Request body

| Parameter        | Type           | Description  |
| :------------- |:-------------| :-----|
| username      | string | Username preferred by the user. Returns an error if the given username already exists.|
| password      | string | password |

#### Sample Request and Response

##### Sample Request
```
curl --location --request POST 'http://localhost:8081/recipefinder/account' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username" : "user",
    "password":"mypass"
}'
```

##### Sample Response
```
75775cb4-2b28-430c-a58d-886cfd9d261b
```

### Save Recipe
Allows users to save recipes to their account. User must provide their unique key in the request in order to identify themselves.

#### Request

```
POST http://{host}:{port}/recipefinder/myrecipes?apiKey={apiKey}
```
 ##### Request parameter

| Parameter        | Type           | Description  |
| :------------- |:-------------| :-----|
| apiKey      | string | The unique key returned to user as a result of create account request.  |

##### Request body

| Parameter        | Type           | Description  |
| :------------- |:-------------| :-----|
| recipeName      | string | Name of the recipe.|
| recipeUrl      | string | Link where the recipe can be found.  |
| rating      | string | User's personal rating for this recipe.  |

#### Sample Request and Response

##### Sample Request
```
curl --location --request POST 'http://localhost:8081/recipefinder/myrecipes?apiKey=155730ff-316a-46d0-937b-1b755042e3e0' \
--header 'Content-Type: application/json' \
--data-raw '{
    "recipeName":"Open-Faced Sandwich of Spinach, Caramelized Onions, and Roasted Peppers",
    "recipeUrl":"https://ohmyveggies.com/recipe-spinach-caramelized-onion-roasted-pepper-open-faced-sandwiches/",
    "rating":"2"
}'
```

##### Sample Response
```
Recipe Saved
```
