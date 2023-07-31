"""
    Purpose is to match two dna sequences to see if they
    match, and where if they don't
"""

seq1 = 'atgcttcggcaagactcaaaaaata'
seq2 = 'atgcttcggcaagactaaataaata'
def sameString(string1:str, string2:str):
    """
    Checks to see if two strings are same. If not, then
    :return: list of indexes of where strings are different, else None.
    """
    indexes = []
    for i in range(len(string1)):
        try:
            if string1[i] != string2[i]:
                indexes.append(i)
        except IndexError:
            break
    if len(indexes) > 0: return indexes
    else: return None

index = sameString(seq1, seq2)

if index is not None:
    for x in index:
        print(f"String1 index {x}: {seq1[x]}\nString2 index {x}: {seq2[x]}",end="\n\n")

# Or the simplest form of what is happening above

# for i in range(len(seq1)):
#     try:
#         if seq1[i] != seq2[i]:
#             print(f"String1 index {i}: {seq1[i]}\nString2 index {i}: {seq2[i]}",end="\n\n")
#     except IndexError:
#         break