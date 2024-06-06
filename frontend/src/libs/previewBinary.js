const previewBinaryFile = (binaryFileObject) => {
  if (!binaryFileObject) return null
  return URL.createObjectURL(binaryFileObject)
}

const checkBlobURL = async (url) => {
  try {
    const response = await fetch(url)
    return response.ok
  } catch (error) {
    console.error('Error fetching blob URL:', error)
    return false
  }
}

export { previewBinaryFile, checkBlobURL }
