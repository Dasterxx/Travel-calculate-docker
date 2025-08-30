import { defineConfig, splitVendorChunkPlugin } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [
    react(),
    splitVendorChunkPlugin()
  ],
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
