import os

def list_files(startpath, ignore_dirs=set(["target", "node_modules", ".git"])):
    output = []
    for root, dirs, files in os.walk(startpath):
        dirs[:] = [d for d in dirs if d not in ignore_dirs]  # 过滤目录
        level = root.replace(startpath, "").count(os.sep)
        indent = "│   " * level + "├── "
        output.append(f"{indent}{os.path.basename(root)}/")
        subindent = "│   " * (level + 1) + "├── "
        for f in files:
            output.append(f"{subindent}{f}")
    return "\n".join(output)

project_structure = list_files("src/main/java/com/yunzhi/retailmanagementsystem")
print(project_structure)
