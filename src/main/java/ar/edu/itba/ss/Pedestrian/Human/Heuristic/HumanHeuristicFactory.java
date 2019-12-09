package ar.edu.itba.ss.Pedestrian.Human.Heuristic;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HumanHeuristicFactory {

    private final static Map<HumanHeuristicType, Class> humanHeuristicEnumMap = Stream.of(
            Pair.of(HumanHeuristicType.MAGNETIC, MagneticHeuristic.class),
            Pair.of(HumanHeuristicType.FIXED_MAGNETIC, FixedMagneticHeuristic.class),
            Pair.of(HumanHeuristicType.LINEAR, LinearHeuristic.class),
            Pair.of(HumanHeuristicType.RULED, RuledBaseHeuristic.class),
            Pair.of(HumanHeuristicType.CUSTOM, CustomHeuristic.class))
                .collect(Collectors.collectingAndThen(Collectors.toMap(Pair::getKey, Pair::getValue), Collections::unmodifiableMap));

    private static HumanHeuristicType humanHeuristicType = HumanHeuristicType.FIXED_MAGNETIC;

    public static HumanHeuristic newHumanHeuristic() throws IllegalAccessException, InstantiationException {
        Constructor<?> constructor = null;

        try {
            constructor = humanHeuristicEnumMap.get(humanHeuristicType).getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        HumanHeuristic resp = null;
        try {
            if (constructor != null)
                resp = (HumanHeuristic) constructor.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static void setHumanHeuristicType(HumanHeuristicType humanHeuristicType) {
        HumanHeuristicFactory.humanHeuristicType = humanHeuristicType;
    }

    public enum HumanHeuristicType {
        MAGNETIC,
        FIXED_MAGNETIC,
        LINEAR,
        RULED,
        CUSTOM;
    }

}
