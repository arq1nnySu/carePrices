package ar.edu.unq.lids.arq2.exceptions

import ar.edu.unq.lids.arq2.service.Resource

case class DuplicateResourceException(resource:Resource) extends RuntimeException