# Using the Utilities package

The `/whisk.system/utils` package offers some utility methods.

The package includes the following feed:

| Entity | Type | Parameters | Description |
| --- | --- | --- | --- |
| `/whisk.system/utils` | package | | Utility methods |
| `/whisk.system/utils/echo` | action | payload | Return the input |
| `/whisk.system/utils/cat` | action | lines | Concatenates input into a string |
| `/whisk.system/utils/split` | action | payload, separator | Split a string into an array |
| `/whisk.system/utils/sort` | action | lines | Sorts an array |
| `/whisk.system/utils/head` | action | lines, num | Extract prefix of an array |
| `/whisk.system/utils/date` | action | | Return current date and time |

## Returning the input

The `/whisk.system/utils/echo` action returns the input as output. The parameter is as follow:
- `payload`: Any JSON entity.

The following is an example of invoking echo action:
  ```
  $ wsk action invoke /whisk.system/utils/echo --blocking --result --param payload 'Hello OpenWhisk'
  ```

  ```
  {
      "payload": "Hello OpenWhisk"
  }
  ```
## Concatenating lines
The `/whisk.system/utils/cat` action concatenates input into a string. The parameter is as follow:
- `lines`: An array of strings or numbers.

The following is an example of invoking cat action:
  ```
  $ wsk action invoke /whisk.system/utils/cat --blocking --result --param lines '[seven,eight,nine]'
  ```

  ```
  {
      "payload": "seven\neight\nnine"
  }
  ```
## Spliting a string
The `/whisk.system/utils/split` action splits a string into an array. The parameter is as follow:
- `payload`: A string.
- `separator`: The character, or the regular expression, to use for splitting the string.

The following is an example of invoking split action:
  ```
  $ wsk action invoke /whisk.system/utils/split --blocking --result --param payload 'seven,eight,nine' --param separator ','
  ```

  ```
  {
      "lines": ["seven","eight","nine"]
      "payload": "seven,eight,nine"
  }
  ```
## Sorting an array
The `/whisk.system/utils/sort` action sorts an array in alphabetical order. The parameter is as follow:
- `lines`: An array of strings.

The following is an example of invoking sort action:
  ```
  $ wsk action invoke /whisk.system/utils/sort --blocking --result --param lines '[seven,eight,nine]'
  ```

  ```
  {
      "lines": ["eight","nine","seven"]
      "length": 3
  }
  ```
## Extracting prefix of an array
The `/whisk.system/utils/head` action sorts an array in alphabetical order. The parameter is as follow:
- `lines`: An array of strings.
- `num`: The length of the prefix.

The following is an example of invoking head action:
  ```
  $ wsk action invoke /whisk.system/utils/head --blocking --result --param lines '[seven,eight,nine]' --param num '2'
  ```

  ```
  {
      "lines": ["seven","eight"]
      "num": 2
  }
  ```
## Returning the current date and time
The `/whisk.system/utils/date` action returns current date and time. 

The following is an example of invoking date action:
  ```
  $ wsk action invoke /whisk.system/utils/date --blocking --result
  ```

  ```
  {
      "date": "2016-08-22T00:59:55.961Z"
  }
  ```
