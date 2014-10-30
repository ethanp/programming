#pragma once
#include <memory>

namespace std { 
template <typename Value, typename ... Arguments>
std::unique_ptr<Value> make_unique(Arguments && ... arguments_for_constructor)
{
    return std::unique_ptr<Value>(
        new Value(std::forward<Arguments>(arguments_for_constructor)...)
    );
}
}



template <typename T_SRC, typename T_DEST, typename T_DELETER>
bool dynamic_pointer_move(std::unique_ptr<T_DEST, T_DELETER> & dest,
                          std::unique_ptr<T_SRC, T_DELETER> & src) {
    if (!src) {
        dest.reset();
        return true;
    }

    T_DEST * dest_ptr = dynamic_cast<T_DEST *>(src.get());
    if (!dest_ptr)
        return false;

    std::unique_ptr<T_DEST, T_DELETER> dest_temp(dest_ptr, src.get_deleter());
    src.release();
    dest.swap(dest_temp);
    return true;
}

template <typename T_SRC, typename T_DEST>
bool dynamic_pointer_move(std::unique_ptr<T_DEST> & dest,
                          std::unique_ptr<T_SRC> & src) {
    if (!src) {
        dest.reset();
        return true;
    }

    T_DEST * dest_ptr = dynamic_cast<T_DEST *>(src.get());
    if (!dest_ptr)
        return false;

    src.release();
    dest.reset(dest_ptr);
    return true;
}
