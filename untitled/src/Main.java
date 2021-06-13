
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        ArrayList<Integer> array = new ArrayList<>();
        int start = 1;
        int elements = 100;
        int a = 1;

        array.add(start);
        for(int i=0;i<elements-1;i++){
            array.add(array.get(i)+a);
        }
        System.out.println(array.toString());

//        for(int i=0;i<elements-1; i++){
//
//            if((i+1)%3==0){
//                int value = array.get(i);
//                array.remove(i);
//                array.add(i,value*value);
//            }
//        }
//        System.out.println(array.toString());
        //wyświetla wszytskie oprócz podzielnych przez 3 ale jeśli są podzielne przez 3*7 to też

        array.stream().filter(element -> !(element%3==0 && element%21!=0 )).forEach(System.out::println);
    }
}
