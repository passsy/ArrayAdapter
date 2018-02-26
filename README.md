![License](https://img.shields.io/badge/license-Apache%202-green.svg?style=flat) [![codecov](https://codecov.io/gh/passsy/ArrayAdapter/branch/master/graph/badge.svg)](https://codecov.io/gh/passsy/ArrayAdapter)
# ArrayAdapter for RecyclerView

`RecyclerView` doesn't ship a ready to go `RecyclerView.Adapter` implementation as `ListView` did with the `ArrayAdapter`. This library is this missing `ArrayAdapter`.

- Easy to use
- Familiar API known from `ArrayAdapter` for `ListView` 
- Notifies the `RecyclerView` correctly about changes (`notifyItem*()`) for smooth animations
- Use `swap(List newItems)` to swap data. `DiffUtils` takes care of `notifyItem*()` calls

## Download

```gradle
dependencies {
    implementation 'com.pascalwelsch.arrayadapter:arrayadapter:1.3.0'
}
```

## Usage

Given a [`User` pojo](https://gist.github.com/passsy/071890e492d2616644cd93c579ab7cf0) with a correct `equals`/`hashCode` implementation is used as data type for `UserAdapter`:

```java
public class UserAdapter extends ArrayAdapter<User, UserAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleView;

        public ViewHolder(final View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Nullable
    @Override
    public Object getItemId(@NonNull final User user) {
        return user.getId();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User item = getItem(position);
        holder.titleView.setText(item.getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_user, parent, false);
        return new ViewHolder(view);
    }
}
```

## Power User APIs

This `ArrayAdapter` implementation internally uses `DiffUtils` to detect changes on items.
Two methods are relevant for this magic which you may want to override:

### `boolean isItemTheSame(old, new)`

checks if the `id` for both items (from `getItemId(T)`) is equal.
Make sure `getItemId(T)` returns a stable id or at least the `item` itself.

If the `id` differs `notifyItemRemoved` for the old and `notifyItemInserted` for the new item gets called, resulting in a call to `onBindViewHolder`.

If the `id` is the same `isContentTheSame(old, new)` will be called.

### `boolean isContentTheSame(old, new)`
checks if the visible content if the item has changed.
This method should return `true` when the visual representation of the items is the same. 
When you only show the `name` of a `User` you should return `true` even when the name fields are equal even when other field are different.

If the content has changed (returns `false`) `notifyItemChanged` will be called triggering a call to `onBindViewHolder`. 

If the content is the same (returns `true`) no notify method is called and `onBindViewHolder` is not called, too.

By default `isContentTheSame` performs an `equal` check on the two item. 
That's why your pojo should implement `equals` correctly and it's recommended to use immutable objects.

Override this method for an optimized change detection.

 
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
