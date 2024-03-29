package enumeration;

import static enumeration.EnumerateWindows.Kernel32.*;
import static enumeration.EnumerateWindows.Psapi.*;
import static enumeration.EnumerateWindows.User32DLL.*;

import java.util.Arrays;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
//import.com.sun.jna.
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

public class EnumerateWindows {
    private static final int MAX_TITLE_LENGTH = 1024;

    public static void printProcess () {
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        GetWindowTextW(GetForegroundWindow(), buffer, MAX_TITLE_LENGTH);
        System.out.println("Active window title: " + Native.toString(buffer));

        int[] obj = {0,0,0,0};
        //Rectangle obj = new Rectangle();
        //Arrays.asList(new String[] { ... });
        //GetWindowRect(GetForegroundWindow(), obj);
        //System.out.println("Active window rect: " + Arrays.toString(obj));
        
        PointerByReference pointer = new PointerByReference();
        GetWindowThreadProcessId(GetForegroundWindow(), pointer);
        Pointer process = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
        GetModuleBaseNameW(process, null, buffer, MAX_TITLE_LENGTH);
        System.out.println("Active window process: " + Native.toString(buffer));
    }
	public static int[] getActiveWinRect () {
		int[] obj = {0,0,0,0};
        //Rectangle obj = new Rectangle();
        //Arrays.asList(new String[] { ... });
        GetWindowRect(GetForegroundWindow(), obj);
        obj[2] = obj[2]-obj[0];
        obj[3] = obj[3]-obj[1];
        return obj;
        //System.out.println("Active window rect: " + Arrays.toString(obj));
	}

    static class Psapi {
        static { Native.register("psapi"); }
        public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
    }

    static class Kernel32 {
        static { Native.register("kernel32"); }
        public static int PROCESS_QUERY_INFORMATION = 0x0400;
        public static int PROCESS_VM_READ = 0x0010;
        public static native int GetLastError();
        public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
    }

    static class User32DLL {
        static { Native.register("user32"); }
        public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
        public static native HWND GetForegroundWindow();
        public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
        public static native boolean GetWindowRect(HWND hWnd, int[] recto);
    }


}