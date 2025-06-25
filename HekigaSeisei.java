/*
gradlew runClient
Minecraft Java Edition 向けのテキストファイル出力
Date: 2025/06/24
Autor: Yokota Kaoru 
 (Funaoka Programming Class)
*/

package com.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class HekigaSeisei implements ClientTickEvents.EndTick{
    // テキストファイルの要素
    private static final String SUUJI_MOJI_0_9_A_Z_a_z = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 画像イメージのテキストファイル名から文字列型配列を返す
    public List<String> gazouYomitori(String pGazou){
        List<String> gyouzentai = new ArrayList<>();
        try {
            // run ディレクトリを起点
            BufferedReader yomitori = new BufferedReader(new InputStreamReader(new FileInputStream(pGazou), StandardCharsets.UTF_8));
            String ichigyou;
            while ((ichigyou = yomitori.readLine()) != null) {
                gyouzentai.add(ichigyou.trim());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("file not found: " + pGazou);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return gyouzentai;
    }

    public void hekigaSakusei(MinecraftClient pJibun, String gazou, int kaishiX, int kaishiZ, int zougenX, int zougenZ){
        BlockPos genzaichi = pJibun.player.getBlockPos();
        List<String> gyouzentai = gazouYomitori(gazou);
        BlockPos kaishi = genzaichi.add(gyouzentai.get(0).length() / 2 * kaishiX, gyouzentai.size(), gyouzentai.get(0).length() / 2 * kaishiZ);
        for (int tate = 0; tate < gyouzentai.size(); tate++) {
            String ichigyou = gyouzentai.get(tate);
            for (int yoko = 0; yoko < ichigyou.length(); yoko++) {
                char moji = ichigyou.charAt(yoko);
                int narabijun = SUUJI_MOJI_0_9_A_Z_a_z.indexOf(moji);
                if (narabijun >= 0 && narabijun < Iro62Shurui.IRO_62_SHURUI.length) {
                    Block masume = Iro62Shurui.IRO_62_SHURUI[narabijun];
                    BlockPos basho = kaishi.add(yoko * zougenX, -tate, yoko * zougenZ);
                    pJibun.world.setBlockState(basho, masume.getDefaultState());
                }
            }
        }
    }

    @Override
    public void onEndTick(MinecraftClient pJibun){
        final String[] GAZOU_NAMAE = {
            "hekigaMinami.txt",
            "hekigaNishi.txt",
            "hekigaKita.txt",
            "hekigaHigashi.txt"
        };
        final int[][] GAZOU_ICHI = {
            {  1,  1, -1,  0 },
            { -1,  1,  0, -1 },
            { -1, -1,  1,  0 },
            {  1, -1,  0,  1 }
        };        
        // マウス左クリック
        if (pJibun.world != null && pJibun.player != null && pJibun.mouse.wasLeftButtonClicked()){
            int kakudo = ((int)pJibun.player.getYaw() + 360 + 45) % 360;
            int hougaku = kakudo / 90;
            System.out.println("degree (kakudo):" + kakudo);
            hekigaSakusei(pJibun, GAZOU_NAMAE[hougaku], GAZOU_ICHI[hougaku][0], GAZOU_ICHI[hougaku][1], GAZOU_ICHI[hougaku][2], GAZOU_ICHI[hougaku][3]);
        }
    }
}
