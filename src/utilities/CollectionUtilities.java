package utilities;

import java.util.List;

public final class CollectionUtilities {

    private CollectionUtilities(){

    }

    public static boolean areCollectionsEqual(final List<?> first, final List<?> second){
        if (first.size() != second.size()){
            return false;
        }

        final int size = first.size();

        for (int i=0; i<size; i++){
            if (!first.get(i).equals(second.get(i))){
                return false;
            }
        }

        return true;
    }
}
