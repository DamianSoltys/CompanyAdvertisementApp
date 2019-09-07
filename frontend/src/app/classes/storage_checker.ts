export function storage_Avaliable(type) {
    let storage;
    let x = '__storage_test__';
    storage = window[type];
    storage.setItem(x, x);
    if(storage.getItem(x) === x) {
        storage.removeItem(x);
        return true;
    } else {
        storage.removeItem(x);
        return false;
    }
}