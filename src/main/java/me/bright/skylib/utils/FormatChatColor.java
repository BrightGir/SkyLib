package me.bright.skylib.utils;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatChatColor {

//   private final static Pattern HEX_COLORS_PATTERN = Pattern.compile("\\{#([a-fA-F0-9]{6})}");
//   private final static Pattern HEX_GRADIENT_PATTERN = Pattern.compile("\\{#([a-fA-F0-9]{6})(:#([a-fA-F0-9]{6}))+( )([^{}])*(})");
//   private final static Pattern HEX_SPIGOT_PATTERN = Pattern.compile("§[xX](§[a-fA-F0-9]){6}");

//   private final static List<ChatColor> FORMAT_COLORS = Arrays.asList(ChatColor.BOLD,
//           ChatColor.ITALIC, ChatColor.UNDERLINE, ChatColor.MAGIC, ChatColor.STRIKETHROUGH, ChatColor.RESET);

//   public static ChatColor parseChatColor(String string) {
//       Preconditions.checkArgument(string != null, "string cannot be null");
//       if (string.startsWith("#") && string.length() == 7) {
//           return ChatColor.of(string);
//       }

//       return ChatColor.valueOf(string);
//   }

//   public static boolean isColor(ChatColor color) {
//       for (ChatColor formatColor : FORMAT_COLORS) {
//           if (formatColor == color) {
//               return false;
//           }
//       }

//       return true;
//   }

//   public static boolean isFormat(ChatColor color) {
//       return !isColor(color);
//   }

//   /**
//    * Removes spigot hex-codes from string
//    *
//    * @param str string to strip hex
//    * @return stripped string
//    */
//   public static String stripHex(String str) {
//       if (str == null) {
//           return null;
//       }

//       Matcher matcher = HEX_SPIGOT_PATTERN.matcher(str);
//       return matcher.replaceAll("");
//   }

//   /**
//    * Finds simple and gradient hex patterns in string and converts it to Spigot format
//    *
//    * @param text string to stylish
//    * @return stylished string
//    */
//   public static String stylish(String text) {
//       if (text == null) {
//           return null;
//       }

//       Matcher matcher = HEX_GRADIENT_PATTERN.matcher(text);

//       StringBuffer stringBuffer = new StringBuffer();

//       while (matcher.find()) {
//           String gradient = matcher.group();

//           int groups = 0;
//           for (int i = 1; gradient.charAt(i) == '#'; i += 8) {
//               groups++;
//           }

//           Color[] colors = new Color[groups];
//           for (int i = 0; i < groups; i++) {
//               colors[i] = ChatColor.of(gradient.substring((8 * i) + 1, (8 * i) + 8)).getColor();
//           }

//           String substring = gradient.substring((groups - 1) * 8 + 9, gradient.length() - 1);

//           char[] chars = substring.toCharArray();

//           StringBuilder gradientBuilder = new StringBuilder();

//           int colorLength = chars.length / (colors.length - 1);
//           int lastColorLength;
//           if (colorLength == 0) {
//               colorLength = 1;
//               lastColorLength = 1;
//               colors = Arrays.copyOfRange(colors, 0, chars.length);
//           } else {
//               lastColorLength = chars.length % (colorLength * (colors.length - 1)) + colorLength;
//           }

//           List<ChatColor> currentStyles = new ArrayList<>();
//           for (int i = 0; i < (colors.length - 1); i++) {
//               int currentColorLength = ((i == colors.length - 2) ? lastColorLength : colorLength);
//               for (int j = 0; j < currentColorLength; j++) {
//                   Color color = calculateGradientColor(j + 1, currentColorLength, colors[i], colors[i + 1]);
//                   ChatColor chatColor = ChatColor.of(color);

//                   int charIndex = colorLength * i + j;
//                   if (charIndex + 1 < chars.length) {
//                       if (chars[charIndex] == '&' || chars[charIndex] == '§') {
//                           if (chars[charIndex + 1] == 'r') {
//                               currentStyles.clear();
//                               j++;
//                               continue;
//                           }

//                           ChatColor style = ChatColor.getByChar(chars[charIndex + 1]);
//                           if (style != null) {
//                               currentStyles.add(style);
//                               j++;
//                               continue;
//                           }
//                       }
//                   }

//                   StringBuilder builder = gradientBuilder.append(chatColor.toString());

//                   for (ChatColor currentStyle : currentStyles) {
//                       builder.append(currentStyle.toString());
//                   }

//                   builder.append(chars[charIndex]);
//               }
//           }

//           matcher.appendReplacement(stringBuffer, gradientBuilder.toString());
//       }

//       matcher.appendTail(stringBuffer);
//       text = stringBuffer.toString();

//       matcher = HEX_COLORS_PATTERN.matcher(text);
//       stringBuffer = new StringBuffer();

//       while (matcher.find()) {
//           String hexColorString = matcher.group();
//           matcher.appendReplacement(stringBuffer, ChatColor.of(hexColorString.substring(1, hexColorString.length() - 1)).toString());
//       }

//       matcher.appendTail(stringBuffer);

//       return ChatColor.translateAlternateColorCodes('&', stringBuffer.toString());
//   }

//   private static Color calculateGradientColor(int x, int parts, Color from, Color to) {
//       double p = (double) (parts - x + 1) / (double) parts;

//       return new Color(
//               (int) (from.getRed() * p + to.getRed() * (1 - p)),
//               (int) (from.getGreen() * p + to.getGreen() * (1 - p)),
//               (int) (from.getBlue() * p + to.getBlue() * (1 - p))
//       );
//   }

}