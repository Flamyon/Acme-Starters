# Acme-Starters-C

Sistema web de información para **Acme Starters, Inc.** (empresa ficticia) orientado a gestionar su negocio y publicar contenido (inventions/campaigns/strategies/sponsorships/audit reports) según el rol del usuario.

## Alcance (Nivel C)
Este repositorio implementa los requisitos obligatorios del **Nivel C**, incluyendo:
- Inicialización del proyecto “Hello-World” como **Acme-Starters-C**.
- Datos de prueba con **2 cuentas administrador**:
  - `administrator1/administrator1`
  - `administrator2/administrator2`
- Datos de prueba con **3 cuentas por rol**:
  - Inventor: `inventor1/inventor1`, `inventor2/inventor2`, `inventor3/inventor3`
  - Spokesperson: `spokesperson1/spokesperson1`, `spokesperson2/spokesperson2`, `spokesperson3/spokesperson3`
  - Fundraiser: `fundraiser1/fundraiser1`, `fundraiser2/fundraiser2`, `fundraiser3/fundraiser3`
  - Sponsor: `sponsor1/sponsor1`, `sponsor2/sponsor2`, `sponsor3/sponsor3`
  - Auditor: `auditor1/auditor1`, `auditor2/auditor2`, `auditor3/auditor3`
- Interfaz disponible en **inglés y español**, con internacionalización correcta de booleanos, fechas/horas, dinero y números.
- Implementación del modelo de datos de cada estudiante (Model-C01…C05) y las restricciones de `Model-CG.ufx`.

> Nota: En informes de la asignatura no se incluyen nombres ni identificadores del grupo; si se referencia a alguien, se usa “Student 1..5”.

## Funcionalidad por roles (resumen)
- **Cualquier principal**: listar/mostrar entidades **publicadas** y navegar a sus partes (y ver el perfil asociado).
- **Principal autenticado**: adquirir el rol correspondiente si aún no lo tiene, y actualizar su perfil de rol.
- **Rol específico (inventor/spokesperson/fundraiser/sponsor/auditor)**:
  - gestionar sus entidades (crear/editar/borrar) y sus partes,
  - **publicar** (bloquea edición/borrado y publica automáticamente las partes).

## Changelog
Ver `CHANGELOG.md`.

## Licencia
MIT (ver `LICENSE`).
