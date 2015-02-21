package ru.freedomlogic.kgcmserver.utils

import org.apache.commons.cli.Options
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.BasicParser
import org.apache.commons.cli.ParseException
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.OptionBuilder
import org.apache.commons.cli.Option

object CmdUtils {

    val ARG_INTERACTIVE = "i"
    val ARG_SEND        = "send"
    val ARG_APIKEY      = "apikey"
    val ARG_REGIDS      = "regids"
    val ARG_GUI         = "gui"
    val ARG_CONFIGURE   = "configure"

    fun parse(args: Array<String>): CommandLine? {
        var cmd: CommandLine? = null

        try {
            cmd = BasicParser().parse(getOptions(), args)
        } catch(e: ParseException) {
            HelpFormatter().printHelp("kGCMServer", getOptions())
        }

        return cmd
    }

    fun getOptions(): Options {
        val options = Options()

        with(options) {
            addOption(ARG_INTERACTIVE, false, "Run in interactive mode")
            addOption(ARG_SEND, false, "Send a typed message")
            addOption(ARG_APIKEY, true, "GCM api key")
            addOption(regIdsOption())
            addOption(ARG_GUI, false, "Run in graphical mode")
            addOption(ARG_CONFIGURE, false, "Create config")

        }

        return options
    }

    private fun apiKeyOption(): Option {
        OptionBuilder.withArgName("key")
        OptionBuilder.withDescription("GCM API key")
        OptionBuilder.hasArg()

        return OptionBuilder.create("apikey")
    }

    private fun regIdsOption(): Option {
        OptionBuilder.withArgName("id1 id2 id3 ... idN")
        OptionBuilder.withDescription("Android device ids")
        OptionBuilder.hasArgs(Option.UNLIMITED_VALUES)

        return OptionBuilder.create("regids")
    }
}
