# Guide de Déploiement - Gigs App

## Variables d'environnement

Définir ces variables avant chaque session de déploiement :

```bash
export PROJECT_ID="gigs-app-474018"
export REGION="europe-west1"
export FRONTEND_IMAGE_PATH="europe-west1-docker.pkg.dev/gigs-app-474018/gigs-app-frontend/gigs-app-frontend_angular"
export BACKEND_IMAGE_PATH="europe-west1-docker.pkg.dev/gigs-app-474018/gigs-app-frontend/gigs-app-backend"
```

## Déploiement du Backend (Spring Boot)

```bash
# 1. Aller dans le dossier backend
cd backend-gigs-app

# 2. Build avec Cloud Build
gcloud builds submit --tag $BACKEND_IMAGE_PATH

# 3. Deploy sur Cloud Run
gcloud run deploy springboot-backend \
  --image=$BACKEND_IMAGE_PATH \
  --platform=managed \
  --region=$REGION \
  --allow-unauthenticated \
  --port=8080 \
  --memory=1Gi \
  --timeout=300
```

## Déploiement du Frontend (Angular)

```bash
# 1. Aller dans le dossier frontend
cd gigs-app-angular

# 2. Build avec Cloud Build
gcloud builds submit --tag $FRONTEND_IMAGE_PATH

# 3. Deploy sur Cloud Run
gcloud run deploy angular-frontend \
  --image=$FRONTEND_IMAGE_PATH \
  --platform=managed \
  --region=$REGION \
  --allow-unauthenticated \
  --port=8080
```

## URLs des Services

- **Frontend**: https://angular-frontend-513726246973.europe-west1.run.app
- **Backend**: https://springboot-backend-513726246973.europe-west1.run.app

## Commandes Utiles

### Vérifier l'état des services

```bash
gcloud run services list --region=$REGION
```

### Voir les logs

**Logs Backend :**
```bash
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=springboot-backend" \
  --limit=20 \
  --format="table(timestamp,textPayload)"
```

**Logs Frontend :**
```bash
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=angular-frontend" \
  --limit=20 \
  --format="table(timestamp,textPayload)"
```

### Supprimer un service

```bash
gcloud run services delete <service-name> --region=$REGION
```

## Architecture

```
┌─────────────────┐
│   Angular App   │ (Port 8080)
│   (Frontend)    │
└────────┬────────┘
         │
         │ HTTPS
         ▼
┌─────────────────┐
│  Spring Boot    │ (Port 8080)
│   (Backend)     │
└─────────────────┘
```

## Configuration CORS

Le backend autorise les requêtes depuis :
- `https://angular-frontend-513726246973.europe-west1.run.app` (production)
- `http://localhost:4200` (développement local)

## Notes Importantes

- Les deux services utilisent le port **8080** en interne
- Cloud Run assigne automatiquement un port externe (443 pour HTTPS)
- Les images sont stockées dans **Artifact Registry** (region: europe-west1)
- Utiliser **Cloud Build** pour éviter les problèmes d'architecture ARM64 vs AMD64
- Les déploiements sont gérés avec `--allow-unauthenticated` (accès public)