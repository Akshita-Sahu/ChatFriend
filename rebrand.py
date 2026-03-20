import os
import re

target_dir = r"c:\Users\AKSHITA\Desktop\ChatFriend"

def replace_content(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        return # Skip binary files

    # Basic replacements
    new_content = re.sub(r'LangChat', 'ChatFriend', content)
    new_content = re.sub(r'langchat', 'chatfriend', new_content)
    new_content = re.sub(r'LANGCHAT', 'CHATFRIEND', new_content)

    # Header replacements specifically (since it might vary year)
    new_content = re.sub(r'Copyright \(c\) \d{4} ChatFriend\. TyCoding All Rights Reserved\.', 'Copyright (c) 2025 ChatFriend. Made by Akshita Sahu. All Rights Reserved.', new_content)
    
    # Also if LangChat was not yet replaced in that specific place for some reason
    new_content = re.sub(r'Copyright \(c\) \d{4} LangChat\. TyCoding All Rights Reserved\.', 'Copyright (c) 2025 ChatFriend. Made by Akshita Sahu. All Rights Reserved.', new_content)

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)

def rename_item(path):
    dirname, basename = os.path.split(path)
    new_basename = re.sub(r'LangChat', 'ChatFriend', basename)
    new_basename = re.sub(r'langchat', 'chatfriend', new_basename)
    new_basename = re.sub(r'LANGCHAT', 'CHATFRIEND', new_basename)

    if new_basename != basename:
        new_path = os.path.join(dirname, new_basename)
        os.rename(path, new_path)
        return new_path
    return path

# First, replace contents in all files
for root, dirs, files in os.walk(target_dir):
    dirs[:] = [d for d in dirs if d not in ['.git', 'node_modules', '.idea', 'target', 'dist']]
    for file in files:
        if file.endswith('.py') and 'rebrand' in file:
            continue
        filepath = os.path.join(root, file)
        replace_content(filepath)

# Second, rename directories and files bottom-up to avoid path invalidation
for root, dirs, files in os.walk(target_dir, topdown=False):
    # Filter out ignored directories by checking the path parts
    parts = set(root.split(os.sep))
    if {'.git', 'node_modules', '.idea', 'target', 'dist'} & parts:
        continue
        
    for file in files:
        if file.endswith('.py') and 'rebrand' in file:
            continue
        rename_item(os.path.join(root, file))
    for d in dirs:
        if d not in ['.git', 'node_modules', '.idea', 'target', 'dist']:
            rename_item(os.path.join(root, d))

print("Rebranding script completed successfully.")
