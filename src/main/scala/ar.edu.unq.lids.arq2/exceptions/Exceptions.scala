package ar.edu.unq.lids.arq2.exceptions

import ar.edu.unq.lids.arq2.service.Resource

class InvalidRequest extends  RuntimeException
case class DuplicateResourceException(resource:Resource) extends RuntimeException