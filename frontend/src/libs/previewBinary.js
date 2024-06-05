const previewBinaryFile = (binaryFileObject) => {
  if (!binaryFileObject) return null
  return URL.createObjectURL(binaryFileObject)
}
export { previewBinaryFile }
