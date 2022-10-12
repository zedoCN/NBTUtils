package text;

import main.Utils.BytesUtils;
import main.mc.MCPosInt;
import main.mc.MCRegion;
import main.nbt.CompoundTag;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MCMusic {
    static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    static MCRegion mcRegion = null;
    static int z = 0;
    static int x = 0;
    static CompoundTag c1, c2, c3, cs, cb, cb2;

    static boolean[] playkey = new boolean[25];

    public static void main(String[] args) {
        Sequence sequence = null;

        try {
            mcRegion = new MCRegion(new File("E:\\MineCraft文件\\1.18.2\\.minecraft\\saves\\BadApple\\region"), MCRegion.SAVEMODE_RewritePart);
            sequence = MidiSystem.getSequence(new File("E:\\工程文件\\java\\MCNBT\\src\\text\\Touhou-Bad-Apple-train-20220115212348-nonstop2k.com.mid"));
            mcRegion.setGenerateChunk(mcRegion.getChunk(new MCPosInt(4, 4)));
            cb = mcRegion.getBlockState(new MCPosInt(0, -60, 0));
            cb2 = mcRegion.getBlockState(new MCPosInt(1, -60, 0));
            c1 = mcRegion.getBlockEntitie(new MCPosInt(0, -60, 0));
            c2 = mcRegion.getBlockEntitie(new MCPosInt(1, -60, 0));
            c3 = mcRegion.getBlockEntitie(new MCPosInt(2, -60, 0));
            cs = c2.clone();
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {

            long tickTime = ((sequence.getMicrosecondLength() / sequence.getTickLength())) / 1000;// 1tick 的毫秒时间
            System.out.println("时长:" + sequence.getMicrosecondLength());
            System.out.println("Ticks:" + sequence.getTickLength());
            System.out.println(sequence.getMicrosecondLength() / sequence.getTickLength());
            long Ntick = 0;
            int minK = 999;
            z = 10000;

            //生成轨道
            for (int i = 0; i < (sequence.getMicrosecondLength() / 1000) / 50; i++) {
                mcRegion.setBlockState(new MCPosInt(0, -60, z + i), cb);
                mcRegion.setBlockState(new MCPosInt(1, -60, z + i), cb2);
                mcRegion.setBlockState(new MCPosInt(2, -60, z + i), cb2);
                mcRegion.setBlockEntitie(new MCPosInt(0, -60, z + i), c1);
                mcRegion.setBlockEntitie(new MCPosInt(1, -60, z + i), c2);
                mcRegion.setBlockEntitie(new MCPosInt(2, -60, z + i), c3);
            }


            for (Track track : sequence.getTracks()) {

                for (int i = 0; i < track.size(); i++) {

                    MidiMessage message = track.get(i).getMessage();
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if (sm.getCommand() == ShortMessage.NOTE_ON) {
                            int key = sm.getData1();
                            int octave = (key / 12) - 1;
                            int note = key % 12;

                            if (minK > key)
                                minK = key;
                            playsound(key - 24 - 8 , (int) (track.get(i).getTick() * tickTime));
                            //play(key - 60);
                        } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                            int key = sm.getData1();
                            int octave = (key / 12) - 1;
                            int note = key % 12;

                            if (minK > key)
                                minK = key;

                            //stop(key - 60);
                        }
                    }
                    //System.out.println(track.get(i).getTick());
                    //delay(tickTime * track.get(i).getTick() - Ntick);
                    //Ntick = tickTime * track.get(i).getTick();

                }
            }
            System.out.println("最小音调" + minK);

            delay(100);

            mcRegion.saveMCA();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static void delay(long ms) throws IOException {
        for (long i = 0; i < ms / 40; i++) {


            mcRegion.setBlockState(new MCPosInt(0, -60, z), cb);
            mcRegion.setBlockState(new MCPosInt(1, -60, z), cb2);
            mcRegion.setBlockState(new MCPosInt(2, -60, z), cb2);
            mcRegion.setBlockEntitie(new MCPosInt(0, -60, z), c1);
            mcRegion.setBlockEntitie(new MCPosInt(1, -60, z), c2);
            mcRegion.setBlockEntitie(new MCPosInt(2, -60, z), c3);
            /*for (int j = 0; j < playkey.length; j++) {
                if (playkey[j])
                    playsound(j);
            }*/

            z++;
            x = 0;
        }

    }

    static void playsound(int a, int time) throws IOException {
        //0-24
        int posZ = time / 50;
        int posX = 3;

        while (!"minecraft:air".equals(mcRegion.getBlockState(new MCPosInt(x + posX, -60, z + posZ)).getTag("Name"))) {
            posX++;
        }

        mcRegion.setBlockState(new MCPosInt(x + posX, -60, z + posZ), cb2);
        String block = "";
        if (a < 25) {
            a--;
            block = "notepp:block.note_block.bit_-1";
        } else if (a < 49) {
            block = "block.note_block.bit";
        } else if (a < 73) {
            a++;
            block = "notepp:block.note_block.bit_1";
        }
        ///playsound notepp:block.note_block.bit_1 ambient @a 2.49 -59.00 -0.63 1 2
        cs.setTag("Command", "playsound " + block + " ambient @a ~ ~ ~ 1000 " + String.valueOf((float) Math.pow(2, ((-12.0 + (a % 25)) / 12.0))));

        mcRegion.setBlockEntitie(new MCPosInt(x + posX, -60, z + posZ), cs);
    }

    static void play(int a) {
        if (a < 0)
            a = 0;
        if (a > 24)
            a = 24;
        playkey[a] = true;
    }

    static void stop(int a) {
        if (a < 0)
            a = 0;
        if (a > 24)
            a = 24;
        playkey[a] = false;
    }
}
