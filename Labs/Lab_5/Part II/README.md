This guide shows how to:
1) Install/verify everything needed
2) Compile and run the CUDA program ONLY using Command Prompt (no Visual Studio project)

Project files expected in ONE folder:
- main.cu
- matmul.cu
- matmul.cuh
- inverse.cu
- inverse.cuh
- utils.cuh

------------------------------------------------------------
A) REQUIREMENTS (what must be installed)
------------------------------------------------------------
1) NVIDIA GPU (example: RTX 3060)
2) NVIDIA Driver installed (WDDM driver on Windows is fine)
3) CUDA Toolkit installed (includes nvcc, cuBLAS, cuSOLVER)
4) Microsoft Visual C++ toolchain (host compiler for nvcc)
   - Visual Studio 2019 or 2022 Build Tools recommended (CUDA supports them)
   - IMPORTANT: CUDA may reject newer Visual Studio toolsets (ex: VS 2026)
     If you have VS 2026, install VS 2019/2022 Build Tools too.

------------------------------------------------------------
B) VERIFY YOUR SETUP (before compiling anything)
------------------------------------------------------------
1) Open a normal CMD and check GPU driver:
   nvidia-smi

You should see your GPU listed and a driver version.

2) Check CUDA compiler:
   nvcc --version

You should see something like: release 13.x

3) Check you have a supported MSVC compiler (IMPORTANT):
   You MUST open the correct Developer Command Prompt (x64) for VS 2019 or VS 2022.

------------------------------------------------------------
C) OPEN THE CORRECT TERMINAL (THIS IS THE MOST IMPORTANT STEP)
------------------------------------------------------------
Because nvcc uses MSVC as its host compiler on Windows, you must compile inside a
Developer Command Prompt that sets the correct environment variables.

1) Click Start Menu and search:
   - "Visual Studio 2019 Developer Command Prompt"
     OR
   - "x64 Native Tools Command Prompt for VS 2022"

2) Open it. It should display something like:
   "Environment initialized for: 'x64'"

3) Confirm the compiler version:
   cl

If it says:
- "for x64"  -> OK
- "for x86"  -> WRONG prompt (use x64 prompt)
- MSVC 19.50 (VS 2026) and nvcc errors -> use VS2019/VS2022 prompt instead

------------------------------------------------------------
D) GO TO YOUR PROJECT FOLDER
------------------------------------------------------------
In the Developer Command Prompt, go to the folder where your .cu files are:

Example:
   cd "C:\Users\<YourName>\Documents\...\Lab_5\Part II"

Confirm you see your files:
   dir

You should see: main.cu, matmul.cu, inverse.cu, etc.

------------------------------------------------------------
E) COMPILE (RECOMMENDED COMMAND FOR RTX 3060)
------------------------------------------------------------
RTX 3060 is Ampere => compute capability is SM 86.
To avoid PTX toolchain mismatch errors, compile directly for sm_86:

   nvcc -O2 -arch=sm_86 main.cu matmul.cu inverse.cu -o matrix_ops.exe -lcublas -lcusolver

If you have a different GPU, use the matching SM value:
- RTX 20xx (Turing): sm_75
- RTX 30xx (Ampere): sm_86 (most desktop 3060/3070/3080/3090) or sm_80 (A100)
- RTX 40xx (Ada): sm_89
(If you’re unsure, ask or check your GPU model and we’ll pick the right sm.)

------------------------------------------------------------
F) RUN
------------------------------------------------------------
Run the program:
   matrix_ops.exe

Expected output includes lines similar to:
   [MatMul] Max abs diff GPU vs CPU: 1.431e-06
   Max |A*Ainv - I| = 4.441e-16

These values may vary slightly due to floating point rounding.

------------------------------------------------------------
G) COMMON ERRORS + FIXES
------------------------------------------------------------
1) ERROR:
   "unsupported Microsoft Visual Studio version! Only 2019 to 2022 are supported!"
CAUSE:
   You are using a newer MSVC toolset (ex: VS 2026).
FIX (best):
   Compile from the VS 2019 or VS 2022 Developer Command Prompt (x64).
TEMP WORKAROUND (not recommended unless necessary):
   nvcc -allow-unsupported-compiler ...

2) ERROR:
   "the provided PTX was compiled with an unsupported toolchain."
CAUSE:
   Driver refuses JIT compiling PTX produced by current toolchain.
FIX:
   Compile directly for your GPU architecture (no PTX reliance):
   nvcc -O2 -arch=sm_86 ...

3) ERROR:
   cannot find -lcublas or -lcusolver
CAUSE:
   CUDA Toolkit install is incomplete or environment paths aren’t set.
FIX:
   Reinstall CUDA Toolkit. Ensure you have full Toolkit (not just driver).
   Usually nvcc + libraries come with Toolkit.

4) ERROR:
   nvcc fatal: Cannot find compiler 'cl.exe'
CAUSE:
   You are NOT in a Visual Studio Developer Command Prompt.
FIX:
   Open the VS 2019/2022 Developer Command Prompt (x64) and rebuild.

5) ERROR:
   'file not found: main.cu' (or others)
CAUSE:
   You are in the wrong directory.
FIX:
   cd into the folder containing the .cu files and run dir to confirm.

------------------------------------------------------------
H) CLEANUP (REMOVE FILES CREATED BY BUILD)
------------------------------------------------------------
The build creates:
- matrix_ops.exe   (your program)
Possibly some temporary files during compile.

To remove the executable:
   del matrix_ops.exe

Optional: build into a separate folder to keep your project clean:
   mkdir build
   cd build
   nvcc -O2 -arch=sm_86 ..\main.cu ..\matmul.cu ..\inverse.cu -o matrix_ops.exe -lcublas -lcusolver
   matrix_ops.exe

Then delete the whole build folder if you want:
   cd ..
   rmdir /s /q build

------------------------------------------------------------
I) SAFETY NOTE
------------------------------------------------------------
This program runs normal CUDA computations. It does not modify your GPU or system.
The only artifacts left are the compiled executable and normal temporary compile files.
GPU load is similar to running any compute program and is safe under normal temperatures.