package tech.laihz.treex_server.entity

import java.io.File


class FileType(
        var media: FileMedia = FileMedia(),
        var document: FileDocument = FileDocument()
) {
    class FileMedia(
            var photo: ArrayList<File> = ArrayList<File>(),
            var music:ArrayList<File> = ArrayList<File>(),
            var video: ArrayList<File> = ArrayList<File>()
    )

    class FileDocument(
            var doc: ArrayList<File> = ArrayList<File>(),
            var excel:ArrayList<File> = ArrayList<File>(),
            var powerPoint:ArrayList<File> = ArrayList<File>(),
            var pdf:ArrayList<File> = ArrayList<File>(),
            var markdown: ArrayList<File> = ArrayList<File>()
    )

    class FileArchive(
            var zip: ArrayList<File> = ArrayList<File>(),
            var rar: ArrayList<File> = ArrayList<File>(),
            var Seven7z: ArrayList<File> = ArrayList<File>()
    )
}