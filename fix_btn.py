import re

file_path = 'app/src/main/java/com/example/kompensasi/DashboardMahasiswaActivity.java'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# We will use regex to remove everything from btnCetak instantiation up to content.addView(btnCetak);

pattern = re.compile(r'\s*com\.google\.android\.material\.button\.MaterialButton\s*btnCetak\s*=\s*new\s*com\.google\.android\.material\.button\.MaterialButton\(this\);.*?content\.addView\(btnCetak\);', re.DOTALL)

new_content = pattern.sub('', content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(new_content)

if new_content != content:
    print("Done removing btnCetak")
else:
    print("Pattern not found")

