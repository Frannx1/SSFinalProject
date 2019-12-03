package ar.edu.itba.ss;

import ar.edu.itba.ss.Pedestrian.Entity;

import java.io.*;
import java.util.*;

public final class IOManager
{
//    private static final long BLOCK_BUFFER = 100;
//    public StringBuffer output;
//    private String outputName;
//
//    public IOManager(String fileName) {
//        output = new StringBuffer();
//
//        try {
//            File file = new File(fileName);
//            outputName = file.getName();
//            if (file.createNewFile()) {
//                System.out.println("File was created.");
//            } else {
//                System.out.println("File was not created.");
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public IOManager() {}
//
//    public void handlePartialOutput() {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName, true));
//            writer.write(this.output.toString());
//            writer.flush();
//            writer.close();
//            this.output = new StringBuffer();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        }
//    }
//
//    public void generateOutputFiles(StringBuffer stringBuffer) {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
//            writer.write(stringBuffer.toString());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        }
//    }
//
//    public void generateOutputFiles() {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
//            writer.write(this.output.toString());
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//            System.out.println(e.toString());
//        }
//    }
//
//    public PackParams readInputFiles(String tFile, String dynamicFile, long frame, double dt, IOManager ioT) {
//        if(frame < 0)
//            throw new IllegalArgumentException("Arguments can't be negative");
//        int N;
//        try(Scanner scanner = new Scanner(new FileReader(dynamicFile))) {
//            N = scanner.nextInt();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        long saveCycle = (long) ((1.0/dt) * 0.1);
//        //long nLines = saveCycle * frame;
//        long nLines = frame;
//        List<Entity> entities = loadDynamicFile(dynamicFile, frame * (N + 2), dt);
//        PackParams p = ioT.loadTFile(tFile, nLines, 1);
//        //PackParams p = ioT.loadTFile(tFile, nLines, saveCycle);
//        p.setEntities(entities);
//        return p;
//    }
//
//    public PackParams loadTFile(String file, long nLines,long saveCycles) {
//        String last = null;
//        try (BufferedReader br = transferData(file, nLines, saveCycles)) {
//            last = br.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//        String[] lastArgs = Objects.requireNonNull(last).split(" ");
//        return new PackParams(lastArgs[0], lastArgs[2]);
//    }
//
//    public List<Entity> loadDynamicFile(String file, long nLines, double dt) {
//        try (BufferedReader br = transferData(file, nLines, 1)) {
//            Scanner scanner = new Scanner(br);
//            scanner.useLocale(Locale.US);
//            int i = 0;
//            int N = scanner.nextInt();
//            List<Entity> entities = new ArrayList<>();
//            double x, y, vx, vy;
//            while(i < N) {
//                if(scanner.hasNextDouble())
//                    x = scanner.nextDouble() + 100;
//                else
//                    throw new IllegalArgumentException("Bad file format missing X value");
//                if(scanner.hasNextDouble())
//                    y = scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing Y value");
//                if(scanner.hasNextDouble())
//                    vx = scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing velocity x value");
//                if(scanner.hasNextDouble())
//                    vy = scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing velocity y value");
//                entities.add(new Entity(i++, x, y, vx, vy, Entity.MASS));
//            }
//            i = 0;
//            scanner.nextInt();
//            while(i < N) {
//                if(scanner.hasNextDouble())
//                    scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing X value");
//                if(scanner.hasNextDouble())
//                    scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing Y value");
//                if(scanner.hasNextDouble())
//                    vx = scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing velocity x value");
//                if(scanner.hasNextDouble())
//                    vy = scanner.nextDouble();
//                else
//                    throw new IllegalArgumentException("Bad file format missing velocity y value");
//                Entity current = entities.get(i++);
//            }
//            return entities;
//        } catch(Exception e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException();
//        }
//    }
//
//    public BufferedReader transferData(String src, long nLines, long saveCycle) throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(src));
//        String line;
//        long k = 0;
//        while (k < nLines && (line = br.readLine()) != null) {
//            if (k % saveCycle == 0)
//                this.output.append(line).append('\n');
//            if (++k % (BLOCK_BUFFER * saveCycle) == 0)
//                this.handlePartialOutput();
//        }
//        this.handlePartialOutput();
//        return br;
//    }
//
//    public class PackParams {
//        public double t;
//        public long k;
//        public List<Entity> entities;
//
//        PackParams(String k, String t) {
//            this.t = Double.parseDouble(t);
//            this.k = Long.parseLong(k);
//        }
//
//        public void setEntities(List<Entity> entities) {
//            this.entities = entities;
//        }
//    }
//
//    public static List<List<Double>> loadFundamental(String filePath) throws IOException {
//        Scanner scanner = new Scanner(new File(filePath));
//        List<List<Double>> result = new ArrayList<>();
//        result.add(new ArrayList<>());
//        result.add(new ArrayList<>());
//
//        while(scanner.hasNext()){
//            result.get(0).add(scanner.nextDouble());
//            result.get(1).add(scanner.nextDouble());
//        }
//
//        return result;
//    }

}

