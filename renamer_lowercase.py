import os
from os.path import join
TARGET_DIR = os.path.expanduser('/Users/phil/Documents/Repositories/blinq-android/blinq/src/main/res/drawable-mdpi/')

for filename in os.listdir(TARGET_DIR):
	if not filename.startswith("."):
		fullpath = join(TARGET_DIR, filename)
		fullpath_lower = join(TARGET_DIR, filename.lower())
		print 'renaming ', fullpath, ' to ', fullpath_lower
		os.rename(fullpath, fullpath_lower)