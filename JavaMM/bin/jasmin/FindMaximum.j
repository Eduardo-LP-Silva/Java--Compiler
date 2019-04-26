.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum([I)I
	.limit locals 4

.end method

.method public build_test_arr()I
	.limit locals 0

.end method

.method public get_array()[I
	.limit locals 0

.end method

.method public static main([Ljava/lang/String;)V
	.limit locals 3

	iload_1
	invokevirtual FindMaximum/build_test_arr()I
	invokevirtual ioPlus/printResult(I )V
.end method

