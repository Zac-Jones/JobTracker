# Multi-stage production build for Angular application

ARG NODE_VERSION=22.14.0-alpine
ARG NGINX_VERSION=alpine3.21

# Build stage
FROM node:${NODE_VERSION} AS builder
WORKDIR /app

# Copy package files for dependency caching
COPY package.json package-lock.json ./

# Install dependencies with clean cache
RUN --mount=type=cache,target=/root/.npm npm ci

# Copy source and build application
COPY . .
RUN npm run build

# Production runtime stage
FROM nginxinc/nginx-unprivileged:${NGINX_VERSION} AS runner

USER nginx

# Copy configuration and built assets
COPY nginx.conf /etc/nginx/nginx.conf
COPY --chown=nginx:nginx --from=builder /app/dist/*/browser /usr/share/nginx/html

EXPOSE 8080

ENTRYPOINT ["nginx", "-c", "/etc/nginx/nginx.conf"]
CMD ["-g", "daemon off;"]
