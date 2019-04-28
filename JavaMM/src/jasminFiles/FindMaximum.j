.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum([I)I
	.limit locals 4

	iconst_1
	istore 2

	aload_1
	istore 3

	iload 3
	ireturn
.end method

.method public build_test_arr()I
	.limit locals 0

	aload_0
	putfield FindMaximum/test_arr [I

	bipush 14
	aload_0
	putfield FindMaximum/test_arr [I

	bipush 28
	aload_0
	putfield FindMaximum/test_arr [I

	iconst_0
	aload_0
	putfield FindMaximum/test_arr [I

	iconst_0
	iconst_5
	isub
	aload_0
	putfield FindMaximum/test_arr [I

	bipush 12
	aload_0
	putfield FindMaximum/test_arr [I

	iconst_0
	ireturn
.end method

.method public get_array()[I
	.limit locals 0

	aload_0
	getfield FindMaximum/test_arr [I
	areturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit locals 2

	new FindMaximum
	invokenonvirtual <init>()V
	astore 1

	aload 1
	invokevirtual FindMaximum/build_test_arr()I
	aload 1
	aload 1
	invokevirtual FindMaximum/get_array()[I
	invokevirtual FindMaximum/find_maximum([I)I
	invokevirtual ioPlus/printResult(I)V
	return
.end method

