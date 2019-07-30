package com.example.sample.service;

import com.example.sample.dto.ChangePasswordBody;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordService {

	private Pattern pattern;
	private Matcher matcher;
	private final String PASSWORD_PATTERN = "^(?=[^=%^+]+$)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*]).{18,}$";
	private final String PASSWORD_SPECIAL_CHAR_REGEX = "([!@#$&*]).{0}";
	private final int SPECIAL_CHAR_MAX = 4;

	public boolean changePassword(ChangePasswordBody changeBody) {
		return true;
	}

	public boolean validateNewPassword(ChangePasswordBody changeBody) {

		return true;
	}

	public int getAllMatchesCount(String compareStr) {
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile(PASSWORD_SPECIAL_CHAR_REGEX).matcher(compareStr);
		while (m.find()) {
			allMatches.add(m.group());
		}
		return allMatches.size();
	}

	public boolean validateNewPassword(String oldPassword, String newPassword) {
		if(checkUnwantedSpecialCharacters(newPassword)) {
			if (findDuplicateCharAndCheckNumbers(newPassword) && getAllMatchesCount(newPassword) <= SPECIAL_CHAR_MAX) {
				pattern = Pattern.compile(PASSWORD_PATTERN);
				matcher = pattern.matcher(newPassword);
				if (matcher.matches()) {
					return compareNewPasswordFromOld(oldPassword, newPassword);
				} else return false;
			} else
				return false;
		}
		else
			return false;
	}


	public static void main(String[] args) {
		PasswordService ps = new PasswordService();

		System.out.println(ps.compareNewPasswordFromOld("aB@#rtttridndjsijdijscijsAfnDFFKDX432", "aB@#rtttridndjsijdijscijsAfnDFFKDX432"));

		//System.out.println(ps.findDuplicateCharAndCheckNumbers("a2222B@$t3333tewe1a"));

		//ps.checkUnwantedSpecialCharacters("aB@#rtttridndjsijdij scijsAfnDFFKDX432");

		System.out.println("Agoda123 Hello123 Test@!!".length());
		System.out.println(ps.calculate("Agoda123 Hello123 Test@!!", "Agoda123 Hello123 3@"));
		System.out.println(ps.compareNewPasswordFromOld("Agoda123 Hello123 Test@!!", "Agoda123 Hello123 3@"));

	}

	public boolean compareNewPasswordFromOld(String oldPassword, String newPassword) {
		float length = ((float)(oldPassword.length()) * (float)0.2);
		int count = calculate(oldPassword, newPassword);
		if(count > length) {
			return true;
		}
		return false;
	}

	/*static int LCSubStr(char X[], char Y[], int m, int n)
    {
        int LCStuff[][] = new int[m + 1][n + 1];
        int result = 0;

        for (int i = 0; i <= m; i++)
        {
            for (int j = 0; j <= n; j++)
            {
                if (i == 0 || j == 0)
                    LCStuff[i][j] = 0;
                else if (X[i - 1] == Y[j - 1])
                {
                    LCStuff[i][j] = LCStuff[i - 1][j - 1] + 1;
                    result = Integer.max(result, LCStuff[i][j]);
                }
                else
                    LCStuff[i][j] = 0;
            }
        }
        return result;
    } */

	//check duplicated chars and check numbers are 50% or more
	public boolean findDuplicateCharAndCheckNumbers(String password) {
		float length = (float)password.length();

		Map<Character, Integer> map = new HashMap<Character, Integer>();
		float digitCount = 0;
		char charArr[] = password.toCharArray();
		for(char c : charArr) {
			if(map.containsKey(c)) {
				map.put(c, map.get(c)+1);
				if(map.get(c) > SPECIAL_CHAR_MAX)
					return false;
			}
			else
				map.put(c, 1);

			if(c >= 48 && c <= 57) {
				digitCount ++;
				if(digitCount>=length/2)
					return false;
			}
		}
		return true;
	}

	public boolean checkUnwantedSpecialCharacters(String password) {
		String specialCharacters = "%'()+,-./:;<=>?[]^_`{|}";

		for (int i = 0; i < password.length(); i++) {
			if (specialCharacters.contains(Character.toString(password.charAt(i)))) {
				return false;
			}
		}
		return true;
	}


	/* calculate Lavenshtein distance */
	public int calculate(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];

		for (int i = 0; i <= x.length(); i++) {
			for (int j = 0; j <= y.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				}
				else if (j == 0) {
					dp[i][j] = i;
				}
				else {
					dp[i][j] = min(dp[i - 1][j - 1]
									+ costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
							dp[i - 1][j] + 1,
							dp[i][j - 1] + 1);
				}
			}
		}

		return dp[x.length()][y.length()];
	}

	public int costOfSubstitution(char a, char b) {
		return a == b ? 0 : 1;
	}

	public int min(int... numbers) {
		return Arrays.stream(numbers)
				.min().orElse(Integer.MAX_VALUE);
	}
}
