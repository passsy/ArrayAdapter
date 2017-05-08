![License](https://img.shields.io/badge/license-Apache%202-green.svg?style=flat) [![codecov](https://codecov.io/gh/passsy/ArrayAdapter/branch/master/graph/badge.svg)](https://codecov.io/gh/passsy/ArrayAdapter)
# ArrayAdapter for RecyclerView

`RecyclerView` doesn't ship a ready to go `RecyclerView.Adapter` implementation as `ListView` did with the `ArrayAdapter`. This library is this missing `ArrayAdapter`.

- Easy to use
- Same API as `ArrayAdapter` for `ListView` 
- Notifies the `RecyclerView` correctly about changes (`notifyItem*()`) for smooth animations
- Use `swap(List newItems)` to swap data. `DiffUtils` takes care of `notifyItem*()` calls

## Download

```gradle
dependencies {
    compile 'com.pascalwelsch.arrayadapter:arrayadapater:1.0.0'
}
```


## License

```
Copyright 2017 Pascal Welsch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
