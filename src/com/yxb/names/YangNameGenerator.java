package com.yxb.names;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class YangNameGenerator {

    static int[][] jis = {
        //火木木
        {4,1,1}, 
        {4,1,2},
        {4,2,1},
        {4,2,2}, 

        //火木火
        {4,1,3},
        {4,1,4},
        {4,2,3},
        {4,2,4}, 

        //火木土
        {4,1,5},
        {4,1,6},
        {4,2,5},
        {4,2,6},

        //火火木
        {4,3,1}, 
        {4,3,2}, 
        {4,4,1}, 
        {4,4,2},

        //火土火
        {4,5,3}, 
        {4,5,4}, 
        {4,6,3},
        {4,6,4}, 

        //火土土
        {4,5,5},
        {4,5,6},
        {4,6,5},
        {4,6,6}
      };

    static int[][] banjis = {
        {4,3,3}, 
        {4,3,4},
        {4,4,3},
        {4,4,4},

        {4,5,1},
        {4,5,2},
        {4,6,1},
        {4,6,2}
        };

    public static void main(String[] args) throws IOException {
        generateNames(6, true);
        generateNames(8, true);
        generateNames(11, true);
        generateNames(13, true);
        generateNames(15, true);
        generateNames(16, true);
        generateNames(21, true);
        generateNames(24, true);
        generateNames(25, true);
        generateNames(29, true);
        generateNames(31, true);
        generateNames(32, true);
        generateNames(33, true);
        generateNames(35, true);
        generateNames(37, true);
        generateNames(39, true);
    }

    private static void generateNames(int totalLength, boolean onlyJi) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String[] jin = constructJinArray();
        String[] mu = constructMuArray();
        String[] shui = constructShuiArray();
        String[] tu = constructTuArray();

        generateNames(jin, jin, stringBuilder, totalLength, onlyJi);
        generateNames(jin, mu, stringBuilder, totalLength, onlyJi);
        generateNames(mu, jin, stringBuilder, totalLength, onlyJi);
        generateNames(jin, shui, stringBuilder, totalLength, onlyJi);
        generateNames(shui, jin, stringBuilder, totalLength, onlyJi);
        generateNames(jin, tu, stringBuilder, totalLength, onlyJi);
        generateNames(tu, jin, stringBuilder, totalLength, onlyJi);

        //System.out.println(stringBuilder.toString());

        File nameFile = new File("/home/everest/Documents/Personal/名字/yang/generatedNames.txt." + totalLength);
        if (nameFile.exists()) {
            nameFile.renameTo(new File("/home/everest/Documents/Personal/名字/yang/generatedNames.txt." + totalLength + ".bak"));
        }

        FileWriter fileWriter = new FileWriter(nameFile);
        fileWriter.write(stringBuilder.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    private static void generateNames(String[] zi2, String[] zi3, StringBuilder stringBuilder, int totalLength, boolean onlyJi) {
        int count = 1;
        boolean shouldEnter = true;
        for (int i = 0; i < zi2.length && i < (totalLength -3); i++) {
            String strZi2 = zi2[i];
            if (strZi2 == null || strZi2.trim().length() == 0)
                continue;

            if ( !computeJi(4, i, totalLength -3 - i, onlyJi)) continue;

            if (zi3.length > (totalLength -3 - i)) {
                String strZi3 = zi3[(totalLength -3 - i)];
                if (strZi3 == null || strZi3.trim().length() == 0)
                    continue;

                StringTokenizer strTokenizer2 = new StringTokenizer(strZi2, " ");
                while (strTokenizer2.hasMoreTokens()) {
                    String n2 = strTokenizer2.nextToken();

                    StringTokenizer strTokenizer3 = new StringTokenizer(strZi3, " ");
                    while (strTokenizer3.hasMoreTokens()) {
                        String n3 = strTokenizer3.nextToken();
                        
                        if ((count++) % 15 != 0) {
                            stringBuilder.append("于" + n2 + n3 + "\t");
                            shouldEnter = true;
                        } else {
                            stringBuilder.append("于" + n2 + n3 + "\n");
                            shouldEnter = false;
                        }
                    }
                }
            }
        }
        if (shouldEnter)
            stringBuilder.append("\n");
    }

    private static boolean computeJi(int i1, int i2, int i3, boolean onlyJi) {
        for (int i = 0; i < jis.length; i++) {
            int[] ji = jis[i];
            if (i1 == ji[0] && ((i1+i2)%10) == ji[1] && ((i2+i3)%10) == ji[2])
                return true;
        }
        if (!onlyJi) {
            for (int i = 0; i < banjis.length; i++) {
                int[] banji = banjis[i];
                if (i1 == banji[0] && ((i1+i2)%10) == banji[1] && ((i2+i3)%10) == banji[2])
                    return true;
            }
        }
        return false;
    }

    private static String[] constructJinArray() {
        String[] jin = new String[24];
        jin[3] = "千";
        jin[4] = "兮";
        jin[5] = "正 仟 申 司";
        jin[6] = "如 式 西 曳";
        jin[8] = "初 青 尚";
        jin[9] = "柔 星 俞";
        jin[10] = "纯 书 纾 殊 素";
        jin[12] = "词 然 疏 舒 丝 斯";
        jin[13] = "楚 靖 诗";
        jin[15] = "靓";
        jin[16] = "锦 静";
        jin[19] = "辞 识";
        jin[20] = "译";
        jin[23] = "纤";
        return jin;
    }

    private static String[] constructMuArray() {
        String[] mu = new String[23];
        mu[4] = "元 月";
        mu[5] = "本 可 巧";
        mu[6] = "朵";
        mu[8] = "林 欣 宜";
        mu[9] = "姣 柯 芃 奕";
        mu[10] = "恭 隽 芫 芷";
        mu[11] = "苾 皎 婕 婧 启 苒 若 啬 翊 苑";
        mu[12] = "绛 轲 椋 茗 荃 雅 尧";
        mu[13] = "荷 敬 颀 倾";
        mu[14] = "菡 菁 菀 语";
        mu[15] = "萱";
        mu[16] = "蓉";
        mu[19] = "薇";
        mu[22] = "苏";
        return mu;
    }

    private static String[] constructShuiArray() {
        String[] shui = new String[23];
        shui[3] = "凡 子";
        shui[5] = "禾";
        shui[6] = "百 帆 行";
        shui[7] = "贝 含 希 妤";
        shui[8] = "佩 雨";
        shui[9] = "妍 盈";
        shui[10] = "毕 函";
        shui[11] = "曼 雪";
        shui[13] = "莫 微 熙 湘";
        shui[14] = "碧 菏 郗 溪";
        shui[15] = "颍";
        shui[16] = "霖";
        shui[17] = "点";
        shui[21] = "露";
        shui[22] = "霁";
        return shui;
    }

    private static String[] constructTuArray() {
        String[] tu = new String[20];
        tu[1] = "一";
        tu[4] = "尹";
        tu[6] = "伊 衣 羽";
        tu[8] = "艾 宛";
        tu[9] = "怡";
        tu[11] = "婉 焉";
        tu[12] = "画 岚";
        tu[15] = "娴 逸 缘";
        tu[16] = "嫒 颐";
        tu[18] = "鄢";
        return tu;
    }
}
